package com.example.carmanager.audit.application.consumer;

import com.example.carmanager.audit.domain.entity.AuditLog;
import com.example.carmanager.audit.domain.event.DomainEvent;
import com.example.carmanager.audit.infrastructure.persistence.AuditLogRepository;
import com.example.carmanager.audit.infrastructure.messaging.RabbitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(AuditEventConsumer.class);
    private final AuditLogRepository auditLogs;
    private final ObjectMapper objectMapper;

    public AuditEventConsumer(AuditLogRepository auditLogs, ObjectMapper objectMapper) {
        this.auditLogs = auditLogs;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consume(DomainEvent event) throws JsonProcessingException {
        auditLogs.save(new AuditLog(
                event.eventType(),
                event.entityType(),
                event.entityId(),
                event.timestamp(),
                objectMapper.writeValueAsString(event.payload())
        ));
        log.info("AUDIT_EVENT_CONSUMED eventType={} entityType={} entityId={}",
                event.eventType(), event.entityType(), event.entityId());
    }
}
