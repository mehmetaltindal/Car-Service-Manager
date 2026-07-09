package com.example.carmanager.car.api.controller;

import com.example.carmanager.car.application.service.CarApplicationService;
import com.example.carmanager.car.api.dto.CarDtos.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarApplicationService service;

    public CarController(CarApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> list(@RequestParam(name = "page", required = false) Integer page,
                                                  @RequestParam(name = "size", required = false) Integer size) {
        if (page == null && size == null) {
            return ResponseEntity.ok(service.list());
        }
        Page<CarResponse> result = service.list(pageRequest(page, size));
        return ResponseEntity.ok()
                .headers(paginationHeaders(result))
                .body(result.getContent());
    }

    @PostMapping
    public CarResponse create(@Valid @RequestBody CarCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CarResponse update(@PathVariable("id") Long id, @Valid @RequestBody CarUpdateRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/technical-profile")
    public CarResponse updateTechnicalProfile(@PathVariable("id") Long id,
                                              @Valid @RequestBody CarTechnicalProfileRequest request) {
        return service.updateTechnicalProfile(id, request);
    }

    private PageRequest pageRequest(Integer page, Integer size) {
        int resolvedPage = page == null ? 0 : page;
        int resolvedSize = size == null ? 20 : size;
        if (resolvedPage < 0) {
            throw new ResponseStatusException(BAD_REQUEST, "page must be greater than or equal to 0");
        }
        if (resolvedSize < 1 || resolvedSize > 100) {
            throw new ResponseStatusException(BAD_REQUEST, "size must be between 1 and 100");
        }
        return PageRequest.of(resolvedPage, resolvedSize);
    }

    private HttpHeaders paginationHeaders(Page<?> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        headers.add("X-Page", String.valueOf(page.getNumber()));
        headers.add("X-Size", String.valueOf(page.getSize()));
        headers.add("X-Total-Pages", String.valueOf(page.getTotalPages()));
        return headers;
    }
}
