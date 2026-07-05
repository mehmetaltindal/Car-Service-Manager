package com.example.carmanager.car.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cars", uniqueConstraints = @UniqueConstraint(name = "uk_car_license_plate", columnNames = "licensePlate"))
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String licensePlate;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String brand;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private CarOwner owner;
    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private CarTechnicalProfile technicalProfile;

    protected Car() {
    }

    public Car(String licensePlate, String model, String brand, CarOwner owner) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.brand = brand;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public String getLicensePlate() { return licensePlate; }
    public String getModel() { return model; }
    public String getBrand() { return brand; }
    public CarOwner getOwner() { return owner; }
    public CarTechnicalProfile getTechnicalProfile() { return technicalProfile; }

    public void update(String licensePlate, String model, String brand, CarOwner owner) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.brand = brand;
        this.owner.update(owner.getFullName(), owner.getPhoneNumber(), owner.getEmail());
    }

    public void updateTechnicalProfile(CarTechnicalProfile profile) {
        this.technicalProfile = profile;
        profile.attachTo(this);
    }
}
