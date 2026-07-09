package com.example.carmanager.car.application.service;

import com.example.carmanager.car.api.dto.CarDtos.*;
import com.example.carmanager.car.application.exception.ConflictException;
import com.example.carmanager.car.application.exception.ValidationException;
import com.example.carmanager.car.application.mapper.CarMapper;
import com.example.carmanager.car.domain.entity.CarOperationLog;
import com.example.carmanager.car.domain.enums.OperationResult;
import com.example.carmanager.car.domain.event.DomainEvent;
import com.example.carmanager.car.domain.policy.LicensePlateValidator;
import com.example.carmanager.car.infrastructure.persistence.CarOperationLogRepository;
import com.example.carmanager.car.infrastructure.persistence.CarRepository;
import com.example.carmanager.car.infrastructure.messaging.RabbitEventPublisher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CarApplicationService {
    private final CarRepository cars;
    private final CarOperationLogRepository logs;
    private final RabbitEventPublisher events;

    public CarApplicationService(CarRepository cars, CarOperationLogRepository logs, RabbitEventPublisher events) {
        this.cars = cars;
        this.logs = logs;
        this.events = events;
    }

    @Transactional(readOnly = true)
    public List<CarResponse> list() {
        return cars.findAll().stream().map(CarMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<CarResponse> list(Pageable pageable) {
        return cars.findAll(pageable).map(CarMapper::toResponse);
    }

    @Transactional
    public CarResponse create(CarCreateRequest request) {
        var plate = CarMapper.normalizePlate(request.licensePlate());
        validatePlate(plate, "CREATE_CAR", null);
        if (cars.existsByLicensePlate(plate)) {
            log("CREATE_CAR", "CAR", null, OperationResult.CONFLICT, "License plate already exists: " + plate);
            throw new ConflictException("License plate already exists: " + plate);
        }
        var car = cars.save(CarMapper.toCar(request));
        log("CREATE_CAR", "CAR", car.getId(), OperationResult.SUCCESS, "Car created");
        events.publish("car.created", new DomainEvent("CAR_CREATED", "CAR", car.getId(), LocalDateTime.now(),
                Map.of("licensePlate", car.getLicensePlate(), "brand", car.getBrand(), "model", car.getModel())));
        return CarMapper.toResponse(car);
    }

    @Transactional
    public CarResponse update(Long id, CarUpdateRequest request) {
        var car = cars.findById(id).orElseThrow(() -> new EntityNotFoundException("Car not found: " + id));
        var plate = CarMapper.normalizePlate(request.licensePlate());
        validatePlate(plate, "UPDATE_CAR", id);
        if (cars.existsByLicensePlateAndIdNot(plate, id)) {
            log("UPDATE_CAR", "CAR", id, OperationResult.CONFLICT, "License plate already exists: " + plate);
            throw new ConflictException("License plate already exists: " + plate);
        }
        car.update(plate, request.model(), request.brand(), CarMapper.toOwner(request.owner()));
        if (request.technicalProfile() != null) {
            car.updateTechnicalProfile(CarMapper.toProfile(request.technicalProfile()));
        }
        log("UPDATE_CAR", "CAR", car.getId(), OperationResult.SUCCESS, "Car updated");
        events.publish("car.updated", new DomainEvent("CAR_UPDATED", "CAR", car.getId(), LocalDateTime.now(),
                Map.of("licensePlate", car.getLicensePlate(), "brand", car.getBrand(), "model", car.getModel())));
        return CarMapper.toResponse(car);
    }

    private void validatePlate(String plate, String operation, Long entityId) {
        if (!LicensePlateValidator.isValid(plate)) {
            log(operation, "CAR", entityId, OperationResult.VALIDATION_ERROR, "Invalid license plate: " + plate);
            throw new ValidationException("Invalid license plate: " + plate);
        }
    }

    private void log(String operation, String entityType, Long entityId, OperationResult result, String message) {
        logs.save(new CarOperationLog(operation, entityType, entityId, result, message));
    }
}
