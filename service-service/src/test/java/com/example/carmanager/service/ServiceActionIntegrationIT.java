package com.example.carmanager.service;

import com.example.carmanager.service.application.exception.ConflictException;
import com.example.carmanager.service.application.service.ServiceActionApplicationService;
import com.example.carmanager.service.api.dto.ServiceDtos.ServiceActionCreateRequest;
import com.example.carmanager.service.api.dto.ServiceDtos.ServiceActionResponse;
import com.example.carmanager.service.api.dto.ServiceDtos.ServiceActionUpdateRequest;
import com.example.carmanager.service.domain.event.DomainEvent;
import com.example.carmanager.service.domain.enums.ServiceStatus;
import com.example.carmanager.service.infrastructure.messaging.RabbitEventPublisher;
import com.example.carmanager.service.infrastructure.persistence.ServiceActionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ServiceActionIntegrationIT {
    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("service_service_test")
            .withUsername("service_user")
            .withPassword("service_pass");

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceActionApplicationService applicationService;

    @Autowired
    private ServiceActionRepository actions;

    @AfterEach
    void cleanActions() {
        actions.deleteAll();
    }

    @Test
    void createsServiceActionWithCatalogService() throws Exception {
        long serviceId = firstCatalogServiceId();

        mockMvc.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actionJson(101L, "34 SVC 101", serviceId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.car.id").value(101))
                .andExpect(jsonPath("$.car.licensePlate").value("34 SVC 101"))
                .andExpect(jsonPath("$.service.id").value(serviceId))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    void filtersServiceActionsByCarIdAndStatus() throws Exception {
        long serviceId = firstCatalogServiceId();
        JsonNode pending = createAction(201L, "34 SVC 201", serviceId);
        JsonNode inProgress = createAction(202L, "34 SVC 202", serviceId);
        updateAction(inProgress.path("id").asLong(), "IN_PROGRESS", "Started", inProgress.path("version").asLong());

        var byCar = objectMapper.readTree(mockMvc.perform(get("/api/services").param("carId", "201"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString());
        assertThat(byCar).hasSize(1);
        assertThat(byCar.get(0).path("id").asLong()).isEqualTo(pending.path("id").asLong());

        var byStatus = objectMapper.readTree(mockMvc.perform(get("/api/services").param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString());
        assertThat(containsActionId(byStatus, inProgress.path("id").asLong())).isTrue();
    }

    @Test
    void paginatesServiceActionsWithExistingFilters() throws Exception {
        long serviceId = firstCatalogServiceId();
        createAction(701L, "34 SVC 701", serviceId);
        createAction(702L, "34 SVC 702", serviceId);
        createAction(703L, "34 SVC 703", serviceId);

        mockMvc.perform(get("/api/services")
                        .param("status", "PENDING")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "3"))
                .andExpect(header().string("X-Page", "0"))
                .andExpect(header().string("X-Size", "2"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void invalidStatusTransitionReturnsBadRequest() throws Exception {
        long serviceId = firstCatalogServiceId();
        JsonNode created = createAction(301L, "34 SVC 301", serviceId);

        mockMvc.perform(put("/api/services/{id}", created.path("id").asLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson("DONE", "Cannot finish directly", created.path("version").asLong())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void staleVersionUpdateReturnsConflict() throws Exception {
        long serviceId = firstCatalogServiceId();
        JsonNode created = createAction(401L, "34 SVC 401", serviceId);
        updateAction(created.path("id").asLong(), "IN_PROGRESS", "Started", created.path("version").asLong());

        mockMvc.perform(put("/api/services/{id}", created.path("id").asLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson("DONE", "Stale update", created.path("version").asLong())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Service action was updated by another user. Refresh the row and try again."));
    }

    @Test
    void finishedAtIsSetOnlyWhenStatusBecomesDone() throws Exception {
        long serviceId = firstCatalogServiceId();
        JsonNode created = createAction(501L, "34 SVC 501", serviceId);

        JsonNode inProgress = updateAction(created.path("id").asLong(), "IN_PROGRESS", "Started", created.path("version").asLong());
        assertThat(inProgress.path("finishedAt").isNull()).isTrue();

        JsonNode done = updateAction(created.path("id").asLong(), "DONE", "Completed", inProgress.path("version").asLong());
        assertThat(done.path("finishedAt").asText()).isNotBlank();
    }

    @Test
    void concurrentTransitionsToInProgressNeverExceedTwoPerCar() throws Exception {
        long serviceId = firstCatalogServiceId();
        long carId = 601L;
        List<ServiceActionResponse> created = List.of(
                applicationService.create(new ServiceActionCreateRequest(carId, "34 SVC 601", serviceId)),
                applicationService.create(new ServiceActionCreateRequest(carId, "34 SVC 601", serviceId)),
                applicationService.create(new ServiceActionCreateRequest(carId, "34 SVC 601", serviceId))
        );

        var executor = Executors.newFixedThreadPool(created.size());
        var ready = new CountDownLatch(created.size());
        var start = new CountDownLatch(1);
        var futures = created.stream()
                .map(action -> executor.submit(() -> transitionToInProgress(action, ready, start)))
                .toList();

        assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
        start.countDown();

        List<String> results = new ArrayList<>();
        for (var future : futures) {
            results.add(future.get(10, TimeUnit.SECONDS));
        }
        executor.shutdown();
        assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();

        assertThat(results).contains("CONFLICT");
        assertThat(actions.findByCarIdAndStatus(carId, ServiceStatus.IN_PROGRESS)).hasSizeLessThanOrEqualTo(2);
    }

    private String transitionToInProgress(ServiceActionResponse action, CountDownLatch ready, CountDownLatch start) throws Exception {
        ready.countDown();
        assertThat(start.await(5, TimeUnit.SECONDS)).isTrue();
        try {
            applicationService.update(action.id(), new ServiceActionUpdateRequest(ServiceStatus.IN_PROGRESS, "Concurrent start", action.version()));
            return "SUCCESS";
        } catch (ConflictException ex) {
            return "CONFLICT";
        }
    }

    private long firstCatalogServiceId() throws Exception {
        JsonNode catalog = objectMapper.readTree(mockMvc.perform(get("/api/services/catalog"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString());
        assertThat(catalog).isNotEmpty();
        return catalog.get(0).path("id").asLong();
    }

    private JsonNode createAction(long carId, String licensePlate, long serviceId) throws Exception {
        String response = mockMvc.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actionJson(carId, licensePlate, serviceId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response);
    }

    private JsonNode updateAction(long id, String status, String report, long version) throws Exception {
        String response = mockMvc.perform(put("/api/services/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson(status, report, version)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response);
    }

    private boolean containsActionId(JsonNode actions, long id) {
        for (JsonNode action : actions) {
            if (action.path("id").asLong() == id) {
                return true;
            }
        }
        return false;
    }

    private String actionJson(long carId, String licensePlate, long serviceId) {
        return """
                {
                  "carId": %d,
                  "carLicensePlate": "%s",
                  "serviceId": %d
                }
                """.formatted(carId, licensePlate, serviceId);
    }

    private String updateJson(String status, String report, long version) {
        return """
                {
                  "status": "%s",
                  "technicianReport": "%s",
                  "version": %d
                }
                """.formatted(status, report, version);
    }

    @TestConfiguration
    static class NoRabbitPublisherConfiguration {
        @Bean
        @Primary
        RabbitEventPublisher noRabbitEventPublisher() {
            return new RabbitEventPublisher(null) {
                @Override
                public void publish(String routingKey, DomainEvent event) {
                    // Integration tests cover HTTP/JPA/locking behavior; audit messaging is tested separately.
                }
            };
        }
    }
}
