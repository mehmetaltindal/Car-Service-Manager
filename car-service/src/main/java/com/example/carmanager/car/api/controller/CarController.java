package com.example.carmanager.car.api.controller;

import com.example.carmanager.car.application.service.CarApplicationService;
import com.example.carmanager.car.api.dto.CarDtos.*;
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
    public CarResponse update(@PathVariable("id") Long id, @Valid @RequestBody CarUpdateRequest request) {
        return service.update(id, request);
    }
}
