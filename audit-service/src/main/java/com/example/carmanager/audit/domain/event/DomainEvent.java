package com.example.carmanager.audit.domain.event;

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
