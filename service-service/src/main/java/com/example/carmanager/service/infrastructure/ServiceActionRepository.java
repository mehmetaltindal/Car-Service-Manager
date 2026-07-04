package com.example.carmanager.service.infrastructure;

import com.example.carmanager.service.domain.ServiceAction;
import com.example.carmanager.service.domain.ServiceStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ServiceActionRepository extends JpaRepository<ServiceAction, Long> {
    List<ServiceAction> findByCarId(Long carId);
    List<ServiceAction> findByStatus(ServiceStatus status);
    List<ServiceAction> findByCarIdAndStatus(Long carId, ServiceStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from ServiceAction a where a.carId = :carId and a.status in :statuses")
    List<ServiceAction> lockByCarIdAndStatuses(@Param("carId") Long carId, @Param("statuses") Collection<ServiceStatus> statuses);
}
