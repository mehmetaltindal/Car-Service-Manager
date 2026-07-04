package com.example.carmanager.audit.application;

import java.time.LocalDateTime;
import java.util.Map;

public record DomainEvent(
        String eventType,
        String entityType,
        Long entityId,
        LocalDateTime timestamp,
        Map<String, Object> payload
) {
}
