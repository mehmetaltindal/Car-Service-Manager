package com.example.carmanager.audit.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventType;
    private String entityType;
    private Long entityId;
    private LocalDateTime timestamp;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String payloadJson;

    protected AuditLog() {
    }

    public AuditLog(String eventType, String entityType, Long entityId, LocalDateTime timestamp, String payloadJson) {
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.timestamp = timestamp;
        this.payloadJson = payloadJson;
    }
}
