package com.example.carmanager.service.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ServiceAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long carId;
    private String carLicensePlate;
    @ManyToOne(optional = false)
    private Service service;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;
    @Column(length = 4000)
    private String technicianReport;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    @Version
    private Long version;

    protected ServiceAction() {
    }

    public ServiceAction(Long carId, String carLicensePlate, Service service) {
        this.carId = carId;
        this.carLicensePlate = carLicensePlate;
        this.service = service;
        this.status = ServiceStatus.PENDING;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void update(ServiceStatus nextStatus, String technicianReport) {
        if (nextStatus != null && nextStatus != this.status) {
            ServiceStatusTransitionPolicy.requireValid(this.status, nextStatus);
            this.status = nextStatus;
            if (nextStatus == ServiceStatus.DONE) {
                finishedAt = LocalDateTime.now();
            }
        }
        if (this.status != ServiceStatus.DONE) {
            finishedAt = null;
        }
        this.technicianReport = technicianReport;
    }

    public Long getId() { return id; }
    public Long getCarId() { return carId; }
    public String getCarLicensePlate() { return carLicensePlate; }
    public Service getService() { return service; }
    public ServiceStatus getStatus() { return status; }
    public String getTechnicianReport() { return technicianReport; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public Long getVersion() { return version; }
}
