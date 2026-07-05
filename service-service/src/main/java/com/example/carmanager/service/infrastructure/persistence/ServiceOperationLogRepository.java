package com.example.carmanager.service.infrastructure.persistence;

import com.example.carmanager.service.domain.entity.ServiceOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOperationLogRepository extends JpaRepository<ServiceOperationLog, Long> {
}
