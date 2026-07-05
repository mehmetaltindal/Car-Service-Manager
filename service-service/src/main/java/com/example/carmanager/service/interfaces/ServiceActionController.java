package com.example.carmanager.service.interfaces;

import com.example.carmanager.service.application.ServiceActionApplicationService;
import com.example.carmanager.service.application.ServiceDtos.*;
import com.example.carmanager.service.domain.ServiceStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceActionController {
    private final ServiceActionApplicationService service;

    public ServiceActionController(ServiceActionApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceActionResponse> list(@RequestParam(name = "carId", required = false) Long carId,
                                            @RequestParam(name = "status", required = false) ServiceStatus status) {
        return service.list(carId, status);
    }

    @GetMapping("/catalog")
    public List<ServiceResponse> catalog() {
        return service.catalog();
    }

    @PostMapping
    public ServiceActionResponse create(@Valid @RequestBody ServiceActionCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ServiceActionResponse update(@PathVariable("id") Long id, @Valid @RequestBody ServiceActionUpdateRequest request) {
        return service.update(id, request);
    }
}
