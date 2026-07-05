package com.example.carmanager.audit.infrastructure.persistence;

import com.example.carmanager.audit.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
