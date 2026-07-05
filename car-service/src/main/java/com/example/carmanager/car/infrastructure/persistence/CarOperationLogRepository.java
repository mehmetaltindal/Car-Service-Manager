package com.example.carmanager.car.infrastructure.persistence;

import com.example.carmanager.car.domain.entity.CarOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOperationLogRepository extends JpaRepository<CarOperationLog, Long> {
}
