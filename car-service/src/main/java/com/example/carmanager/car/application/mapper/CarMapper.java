package com.example.carmanager.car.application.mapper;

import com.example.carmanager.car.api.dto.CarDtos.*;
import com.example.carmanager.car.domain.entity.Car;
import com.example.carmanager.car.domain.entity.CarOwner;
import com.example.carmanager.car.domain.entity.CarTechnicalProfile;

public final class CarMapper {
    private CarMapper() {
    }

    public static Car toCar(CarCreateRequest request) {
        var car = new Car(normalizePlate(request.licensePlate()), request.model(), request.brand(), toOwner(request.owner()));
        if (request.technicalProfile() != null) {
            car.updateTechnicalProfile(toProfile(request.technicalProfile()));
        }
        return car;
    }

    public static CarOwner toOwner(CarOwnerRequest request) {
        return new CarOwner(request.fullName(), request.phoneNumber(), request.email());
    }

    public static CarTechnicalProfile toProfile(CarTechnicalProfileRequest request) {
        return new CarTechnicalProfile(
                request.engineOilType(),
                request.tireBrand(),
                request.tireSize(),
                request.batteryType(),
                request.brakeFluidType(),
                request.transmissionOilType()
        );
    }

    public static CarResponse toResponse(Car car) {
        return new CarResponse(
                car.getId(),
                car.getLicensePlate(),
                car.getModel(),
                car.getBrand(),
                new CarOwnerResponse(
                        car.getOwner().getId(),
                        car.getOwner().getFullName(),
                        car.getOwner().getPhoneNumber(),
                        car.getOwner().getEmail()
                ),
                toProfileResponse(car.getTechnicalProfile())
        );
    }

    public static CarTechnicalProfileResponse toProfileResponse(CarTechnicalProfile profile) {
        if (profile == null) {
            return null;
        }
        return new CarTechnicalProfileResponse(
                profile.getId(),
                profile.getEngineOilType(),
                profile.getTireBrand(),
                profile.getTireSize(),
                profile.getBatteryType(),
                profile.getBrakeFluidType(),
                profile.getTransmissionOilType(),
                profile.getUpdatedAt()
        );
    }

    public static String normalizePlate(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
