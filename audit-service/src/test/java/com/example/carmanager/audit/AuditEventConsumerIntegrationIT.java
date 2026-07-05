package com.example.carmanager.audit;

import com.example.carmanager.audit.application.DomainEvent;
import com.example.carmanager.audit.domain.AuditLog;
import com.example.carmanager.audit.infrastructure.AuditLogRepository;
import com.example.carmanager.audit.infrastructure.RabbitConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class AuditEventConsumerIntegrationIT {
    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("audit_service_test")
            .withUsername("audit_user")
            .withPassword("audit_pass");

    @Container
    static final RabbitMQContainer RABBITMQ = new RabbitMQContainer("rabbitmq:3.13-management");

    @DynamicPropertySource
    static void containerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.rabbitmq.host", RABBITMQ::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ::getAdminPassword);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AuditLogRepository auditLogs;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void cleanAuditLogs() {
        auditLogs.deleteAll();
    }

    @Test
    void consumesRabbitEventAndWritesAuditLog() throws Exception {
        var event = new DomainEvent(
                "SERVICE_ACTION_STATUS_CHANGED",
                "SERVICE_ACTION",
                42L,
                LocalDateTime.now().withNano(0),
                Map.of("previousStatus", "IN_PROGRESS", "currentStatus", "DONE", "carId", 101L)
        );

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "service-action.status-changed", event);

        AuditLog saved = waitForSingleAuditLog();
        assertThat(saved.getEventType()).isEqualTo("SERVICE_ACTION_STATUS_CHANGED");
        assertThat(saved.getEntityType()).isEqualTo("SERVICE_ACTION");
        assertThat(saved.getEntityId()).isEqualTo(42L);
        assertThat(saved.getTimestamp()).isEqualTo(event.timestamp());

        var payload = objectMapper.readTree(saved.getPayloadJson());
        assertThat(payload.path("previousStatus").asText()).isEqualTo("IN_PROGRESS");
        assertThat(payload.path("currentStatus").asText()).isEqualTo("DONE");
        assertThat(payload.path("carId").asLong()).isEqualTo(101L);
    }

    private AuditLog waitForSingleAuditLog() throws InterruptedException {
        for (int attempt = 0; attempt < 40; attempt++) {
            var logs = auditLogs.findAll();
            if (!logs.isEmpty()) {
                assertThat(logs).hasSize(1);
                return logs.get(0);
            }
            Thread.sleep(250);
        }
        throw new AssertionError("Audit log was not written after RabbitMQ event consumption.");
    }
}
