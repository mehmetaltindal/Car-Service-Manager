package com.example.carmanager.service.infrastructure;

import com.example.carmanager.service.domain.ServiceOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOperationLogRepository extends JpaRepository<ServiceOperationLog, Long> {
}
