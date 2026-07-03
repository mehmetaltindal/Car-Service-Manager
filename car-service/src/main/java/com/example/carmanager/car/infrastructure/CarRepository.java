package com.example.carmanager.car.infrastructure;

import com.example.carmanager.car.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByLicensePlate(String licensePlate);
    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);
}
