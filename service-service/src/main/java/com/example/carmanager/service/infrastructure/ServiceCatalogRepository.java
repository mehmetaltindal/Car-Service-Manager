package com.example.carmanager.service.infrastructure;

import com.example.carmanager.service.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCatalogRepository extends JpaRepository<Service, Long> {
    boolean existsByTitle(String title);
}
