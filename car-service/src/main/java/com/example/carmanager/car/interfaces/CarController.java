package com.example.carmanager.car.interfaces;

import com.example.carmanager.car.application.CarApplicationService;
import com.example.carmanager.car.application.CarDtos.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarApplicationService service;

    public CarController(CarApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<CarResponse> list() {
        return service.list();
    }

    @PostMapping
    public CarResponse create(@Valid @RequestBody CarCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CarResponse update(@PathVariable Long id, @Valid @RequestBody CarUpdateRequest request) {
        return service.update(id, request);
    }
}
