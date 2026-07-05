package com.example.carmanager.car;

import com.example.carmanager.car.infrastructure.RabbitEventPublisher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class CarIntegrationIT {
    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("car_service_test")
            .withUsername("car_user")
            .withPassword("car_pass");

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

    @Test
    void createsCarWithRequiredOwnerAndOptionalTechnicalProfile() throws Exception {
        var response = mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "licensePlate": "34 ABC 123",
                                  "model": "Corolla",
                                  "brand": "Toyota",
                                  "owner": {
                                    "fullName": "Mehmet Altindal",
                                    "phoneNumber": "+905551112233",
                                    "email": "mehmet@example.com"
                                  },
                                  "technicalProfile": {
                                    "engineOilType": "5W-30",
                                    "tireBrand": "Michelin",
                                    "tireSize": "205/55 R16",
                                    "batteryType": "AGM",
                                    "brakeFluidType": "DOT4",
                                    "transmissionOilType": "ATF"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.licensePlate").value("34 ABC 123"))
                .andExpect(jsonPath("$.owner.fullName").value("Mehmet Altindal"))
                .andExpect(jsonPath("$.technicalProfile.engineOilType").value("5W-30"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode created = objectMapper.readTree(response);
        assertThat(created.path("technicalProfile").path("updatedAt").asText()).isNotBlank();
    }

    @Test
    void duplicateLicensePlateReturnsConflict() throws Exception {
        createCar("06 DUP 001", "Civic", "Honda", "First Owner");

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson("06 DUP 001", "Civic", "Honda", "Second Owner")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("License plate already exists: 06 DUP 001"));
    }

    @Test
    void updatesCarAndKeepsPublicContract() throws Exception {
        long id = createCar("35 OLD 001", "Focus", "Ford", "Original Owner");

        mockMvc.perform(put("/api/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "licensePlate": "35 NEW 001",
                                  "model": "Megane",
                                  "brand": "Renault",
                                  "owner": {
                                    "fullName": "Updated Owner",
                                    "phoneNumber": "+905559998877",
                                    "email": "updated@example.com"
                                  },
                                  "technicalProfile": {
                                    "engineOilType": "0W-20",
                                    "tireBrand": "Pirelli",
                                    "tireSize": "215/50 R17",
                                    "batteryType": "EFB",
                                    "brakeFluidType": "DOT4",
                                    "transmissionOilType": "CVT"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.licensePlate").value("35 NEW 001"))
                .andExpect(jsonPath("$.model").value("Megane"))
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.owner.fullName").value("Updated Owner"))
                .andExpect(jsonPath("$.technicalProfile.transmissionOilType").value("CVT"));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %s)].licensePlate", id).value("35 NEW 001"));
    }

    private long createCar(String licensePlate, String model, String brand, String ownerName) throws Exception {
        var response = mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson(licensePlate, model, brand, ownerName)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).path("id").asLong();
    }

    private String carJson(String licensePlate, String model, String brand, String ownerName) {
        return """
                {
                  "licensePlate": "%s",
                  "model": "%s",
                  "brand": "%s",
                  "owner": {
                    "fullName": "%s",
                    "phoneNumber": "+905550000000",
                    "email": "owner@example.com"
                  },
                  "technicalProfile": null
                }
                """.formatted(licensePlate, model, brand, ownerName);
    }

    @TestConfiguration
    static class NoRabbitPublisherConfiguration {
        @Bean
        @Primary
        RabbitEventPublisher noRabbitEventPublisher() {
            return new RabbitEventPublisher(null) {
                @Override
                public void publish(String routingKey, com.example.carmanager.car.domain.DomainEvent event) {
                    // Integration tests cover HTTP/JPA behavior; audit messaging is tested separately.
                }
            };
        }
    }
}
