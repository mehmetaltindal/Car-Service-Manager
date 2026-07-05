package com.example.carmanager.service.application;

import com.example.carmanager.service.application.ServiceDtos.*;
import com.example.carmanager.service.domain.*;
import com.example.carmanager.service.infrastructure.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceActionApplicationService {
    private final ServiceActionRepository actions;
    private final ServiceCatalogRepository services;
    private final ServiceOperationLogRepository logs;
    private final RabbitEventPublisher events;

    public ServiceActionApplicationService(ServiceActionRepository actions, ServiceCatalogRepository services,
                                           ServiceOperationLogRepository logs, RabbitEventPublisher events) {
        this.actions = actions;
        this.services = services;
        this.logs = logs;
        this.events = events;
    }

    @Transactional(readOnly = true)
    public List<ServiceActionResponse> list(Long carId, ServiceStatus status) {
        List<ServiceAction> result;
        if (carId != null && status != null) {
            result = actions.findByCarIdAndStatus(carId, status);
        } else if (carId != null) {
            result = actions.findByCarId(carId);
        } else if (status != null) {
            result = actions.findByStatus(status);
        } else {
            result = actions.findAll();
        }
        return result.stream().map(ServiceActionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> catalog() {
        return services.findAll().stream().map(ServiceActionMapper::toServiceResponse).toList();
    }

    @Transactional
    public ServiceActionResponse create(ServiceActionCreateRequest request) {
        var catalogService = services.findById(request.serviceId())
                .orElseThrow(() -> new EntityNotFoundException("Service catalog entry not found: " + request.serviceId()));
        var action = actions.save(new ServiceAction(request.carId(), request.carLicensePlate(), catalogService));
        log("CREATE_SERVICE_ACTION", "SERVICE_ACTION", action.getId(), OperationResult.SUCCESS, "Service action created");
        events.publish("service-action.created", new DomainEvent("SERVICE_ACTION_CREATED", "SERVICE_ACTION", action.getId(),
                LocalDateTime.now(), Map.of("carId", action.getCarId(), "serviceId", catalogService.getId(), "status", action.getStatus().name())));
        return ServiceActionMapper.toResponse(action);
    }

    @Transactional
    public ServiceActionResponse update(Long id, ServiceActionUpdateRequest request) {
        var action = actions.findById(id).orElseThrow(() -> new EntityNotFoundException("Service action not found: " + id));
        if (!action.getVersion().equals(request.version())) {
            log("UPDATE_SERVICE_ACTION", "SERVICE_ACTION", id, OperationResult.CONFLICT, "Stale version: " + request.version());
            throw new ConflictException("Service action was updated by another user. Refresh the row and try again.");
        }
        var previousStatus = action.getStatus();
        try {
            if (request.status() == ServiceStatus.IN_PROGRESS && previousStatus != ServiceStatus.IN_PROGRESS) {
                assertInProgressLimit(action.getCarId());
            }
            action.update(request.status(), request.technicianReport());
        } catch (InvalidStatusTransitionException ex) {
            log("CHANGE_SERVICE_ACTION_STATUS", "SERVICE_ACTION", id, OperationResult.VALIDATION_ERROR, ex.getMessage());
            throw ex;
        } catch (ObjectOptimisticLockingFailureException ex) {
            log("UPDATE_SERVICE_ACTION", "SERVICE_ACTION", id, OperationResult.CONFLICT, ex.getMessage());
            throw new ConflictException("Service action was updated by another user. Refresh the row and try again.");
        }

        actions.flush();
        log("UPDATE_SERVICE_ACTION", "SERVICE_ACTION", action.getId(), OperationResult.SUCCESS, "Service action updated");
        publishUpdate(action);
        if (previousStatus != action.getStatus()) {
            log("CHANGE_SERVICE_ACTION_STATUS", "SERVICE_ACTION", action.getId(), OperationResult.SUCCESS,
                    previousStatus + " -> " + action.getStatus());
            publishStatusChanged(action, previousStatus);
        }
        return ServiceActionMapper.toResponse(action);
    }

    private void assertInProgressLimit(Long carId) {
        var locked = actions.lockByCarIdAndStatuses(carId, List.of(ServiceStatus.PENDING, ServiceStatus.IN_PROGRESS));
        var inProgressCount = locked.stream().filter(action -> action.getStatus() == ServiceStatus.IN_PROGRESS).count();
        if (inProgressCount >= 2) {
            log("CHANGE_SERVICE_ACTION_STATUS", "SERVICE_ACTION", null, OperationResult.CONFLICT,
                    "Max 2 IN_PROGRESS ServiceAction per car exceeded for carId=" + carId);
            throw new ConflictException("A car can have at most 2 IN_PROGRESS service actions.");
        }
    }

    private void publishUpdate(ServiceAction action) {
        events.publish("service-action.updated", new DomainEvent("SERVICE_ACTION_UPDATED", "SERVICE_ACTION", action.getId(),
                LocalDateTime.now(), Map.of("carId", action.getCarId(), "serviceId", action.getService().getId(),
                "status", action.getStatus().name(), "version", action.getVersion())));
    }

    private void publishStatusChanged(ServiceAction action, ServiceStatus previousStatus) {
        var payload = new LinkedHashMap<String, Object>();
        payload.put("previousStatus", previousStatus.name());
        payload.put("currentStatus", action.getStatus().name());
        payload.put("carId", action.getCarId());
        payload.put("serviceId", action.getService().getId());
        payload.put("finishedAt", action.getFinishedAt());
        events.publish("service-action.status-changed", new DomainEvent("SERVICE_ACTION_STATUS_CHANGED", "SERVICE_ACTION", action.getId(),
                LocalDateTime.now(), payload));
    }

    private void log(String operation, String entityType, Long entityId, OperationResult result, String message) {
        logs.save(new ServiceOperationLog(operation, entityType, entityId, result, message));
    }
}
