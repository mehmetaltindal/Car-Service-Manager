package com.example.carmanager.service.api.dto;

import com.example.carmanager.service.domain.enums.ServiceStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public final class ServiceDtos {
    private ServiceDtos() {
    }

    public record ServiceResponse(Long id, String title, String description) {
    }

    public record ServiceActionCreateRequest(
            @NotNull Long carId,
            String carLicensePlate,
            @NotNull Long serviceId
    ) {
    }

    public record ServiceActionUpdateRequest(
            ServiceStatus status,
            String technicianReport,
            @NotNull Long version
    ) {
    }

    public record CarSummary(Long id, String licensePlate) {
    }

    public record ServiceActionResponse(
            Long id,
            CarSummary car,
            ServiceResponse service,
            ServiceStatus status,
            String technicianReport,
            LocalDateTime createdAt,
            LocalDateTime finishedAt,
            Long version
    ) {
    }
}
