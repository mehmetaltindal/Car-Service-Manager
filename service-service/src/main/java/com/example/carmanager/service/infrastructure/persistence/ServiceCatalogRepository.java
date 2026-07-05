package com.example.carmanager.service.infrastructure.persistence;

import com.example.carmanager.service.domain.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCatalogRepository extends JpaRepository<Service, Long> {
    boolean existsByTitle(String title);
}
