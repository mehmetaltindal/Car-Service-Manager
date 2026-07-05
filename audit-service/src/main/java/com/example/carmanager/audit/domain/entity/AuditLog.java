package com.example.carmanager.audit.domain.entity;

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

    public Long getId() { return id; }
    public String getEventType() { return eventType; }
    public String getEntityType() { return entityType; }
    public Long getEntityId() { return entityId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPayloadJson() { return payloadJson; }
}
