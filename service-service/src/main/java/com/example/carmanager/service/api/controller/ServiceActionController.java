package com.example.carmanager.service.api.controller;

import com.example.carmanager.service.application.service.ServiceActionApplicationService;
import com.example.carmanager.service.api.dto.ServiceDtos.*;
import com.example.carmanager.service.domain.enums.ServiceStatus;
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
@RequestMapping("/api/services")
public class ServiceActionController {
    private final ServiceActionApplicationService service;

    public ServiceActionController(ServiceActionApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceActionResponse>> list(@RequestParam(name = "carId", required = false) Long carId,
                                                            @RequestParam(name = "status", required = false) ServiceStatus status,
                                                            @RequestParam(name = "page", required = false) Integer page,
                                                            @RequestParam(name = "size", required = false) Integer size) {
        if (page == null && size == null) {
            return ResponseEntity.ok(service.list(carId, status));
        }
        Page<ServiceActionResponse> result = service.list(carId, status, pageRequest(page, size));
        return ResponseEntity.ok()
                .headers(paginationHeaders(result))
                .body(result.getContent());
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
