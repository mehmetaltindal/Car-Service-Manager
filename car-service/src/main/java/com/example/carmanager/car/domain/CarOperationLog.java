package com.example.carmanager.car.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "car_operation_log")
public class CarOperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String operationName;
    private String entityType;
    private Long entityId;
    @Enumerated(EnumType.STRING)
    private OperationResult result;
    @Column(length = 1024)
    private String message;
    private LocalDateTime createdAt;

    protected CarOperationLog() {
    }

    public CarOperationLog(String operationName, String entityType, Long entityId, OperationResult result, String message) {
        this.operationName = operationName;
        this.entityType = entityType;
        this.entityId = entityId;
        this.result = result;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
