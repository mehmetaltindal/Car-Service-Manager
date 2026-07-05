package com.example.carmanager.service.application.mapper;

import com.example.carmanager.service.api.dto.ServiceDtos.*;
import com.example.carmanager.service.domain.entity.Service;
import com.example.carmanager.service.domain.entity.ServiceAction;

public final class ServiceActionMapper {
    private ServiceActionMapper() {
    }

    public static ServiceResponse toServiceResponse(Service service) {
        return new ServiceResponse(service.getId(), service.getTitle(), service.getDescription());
    }

    public static ServiceActionResponse toResponse(ServiceAction action) {
        return new ServiceActionResponse(
                action.getId(),
                new CarSummary(action.getCarId(), action.getCarLicensePlate()),
                toServiceResponse(action.getService()),
                action.getStatus(),
                action.getTechnicianReport(),
                action.getCreatedAt(),
                action.getFinishedAt(),
                action.getVersion()
        );
    }
}
