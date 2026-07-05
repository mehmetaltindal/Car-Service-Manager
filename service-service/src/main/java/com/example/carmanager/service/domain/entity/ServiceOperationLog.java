package com.example.carmanager.service.domain.entity;

import com.example.carmanager.service.domain.enums.OperationResult;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_operation_log")
public class ServiceOperationLog {
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

    protected ServiceOperationLog() {
    }

    public ServiceOperationLog(String operationName, String entityType, Long entityId, OperationResult result, String message) {
        this.operationName = operationName;
        this.entityType = entityType;
        this.entityId = entityId;
        this.result = result;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
