package com.example.carmanager.service.infrastructure.seed;

import com.example.carmanager.service.domain.entity.Service;
import com.example.carmanager.service.infrastructure.persistence.ServiceCatalogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceCatalogSeeder {
    @Bean
    CommandLineRunner seedServices(ServiceCatalogRepository services) {
        return args -> {
            seed(services, "Oil Change", "Engine oil and filter replacement");
            seed(services, "Inspection", "General vehicle inspection");
            seed(services, "Tire Change", "Tire replacement or rotation");
            seed(services, "Brake Check", "Brake system inspection");
        };
    }

    private void seed(ServiceCatalogRepository services, String title, String description) {
        if (!services.existsByTitle(title)) {
            services.save(new Service(title, description));
        }
    }
}
