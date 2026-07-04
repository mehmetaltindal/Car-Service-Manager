package com.example.carmanager.car.domain;

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
