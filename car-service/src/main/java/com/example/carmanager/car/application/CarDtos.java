package com.example.carmanager.car.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public final class CarDtos {
    private CarDtos() {
    }

    public record CarOwnerRequest(
            @NotBlank String fullName,
            String phoneNumber,
            String email
    ) {
    }

    public record CarOwnerResponse(Long id, String fullName, String phoneNumber, String email) {
    }

    public record CarTechnicalProfileRequest(
            String engineOilType,
            String tireBrand,
            String tireSize,
            String batteryType,
            String brakeFluidType,
            String transmissionOilType
    ) {
    }

    public record CarTechnicalProfileResponse(
            Long id,
            String engineOilType,
            String tireBrand,
            String tireSize,
            String batteryType,
            String brakeFluidType,
            String transmissionOilType,
            LocalDateTime updatedAt
    ) {
    }

    public record CarCreateRequest(
            @NotBlank String licensePlate,
            @NotBlank String model,
            @NotBlank String brand,
            @NotNull @Valid CarOwnerRequest owner,
            @Valid CarTechnicalProfileRequest technicalProfile
    ) {
    }

    public record CarUpdateRequest(
            @NotBlank String licensePlate,
            @NotBlank String model,
            @NotBlank String brand,
            @NotNull @Valid CarOwnerRequest owner,
            @Valid CarTechnicalProfileRequest technicalProfile
    ) {
    }

    public record CarResponse(
            Long id,
            String licensePlate,
            String model,
            String brand,
            CarOwnerResponse owner,
            CarTechnicalProfileResponse technicalProfile
    ) {
    }
}
