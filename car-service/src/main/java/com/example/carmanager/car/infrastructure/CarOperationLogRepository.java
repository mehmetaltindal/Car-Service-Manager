package com.example.carmanager.car.infrastructure;

import com.example.carmanager.car.domain.CarOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOperationLogRepository extends JpaRepository<CarOperationLog, Long> {
}
