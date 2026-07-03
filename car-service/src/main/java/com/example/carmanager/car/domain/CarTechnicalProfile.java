package com.example.carmanager.car.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CarTechnicalProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    private Car car;
    private String engineOilType;
    private String tireBrand;
    private String tireSize;
    private String batteryType;
    private String brakeFluidType;
    private String transmissionOilType;
    private LocalDateTime updatedAt;

    protected CarTechnicalProfile() {
    }

    public CarTechnicalProfile(String engineOilType, String tireBrand, String tireSize, String batteryType,
                               String brakeFluidType, String transmissionOilType) {
        this.engineOilType = engineOilType;
        this.tireBrand = tireBrand;
        this.tireSize = tireSize;
        this.batteryType = batteryType;
        this.brakeFluidType = brakeFluidType;
        this.transmissionOilType = transmissionOilType;
    }

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = LocalDateTime.now();
    }

    void attachTo(Car car) {
        this.car = car;
    }

    public Long getId() { return id; }
    public String getEngineOilType() { return engineOilType; }
    public String getTireBrand() { return tireBrand; }
    public String getTireSize() { return tireSize; }
    public String getBatteryType() { return batteryType; }
    public String getBrakeFluidType() { return brakeFluidType; }
    public String getTransmissionOilType() { return transmissionOilType; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
