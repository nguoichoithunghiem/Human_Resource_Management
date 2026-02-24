package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.PositionDto.PositionRequest;
import com.example.Human_Resource_Management.dto.PositionDto.PositionResponse;
import com.example.Human_Resource_Management.service.PositionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@Validated
public class PositionController {

    private final PositionService positionService;

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PositionResponse create(@Valid @RequestBody PositionRequest request) {
        return positionService.create(request);
    }

    // ADMIN + MANAGER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public List<PositionResponse> getAll() {
        return positionService.getAll();
    }

    // ADMIN + MANAGER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public PositionResponse getById(@PathVariable Long id) {
        return positionService.getById(id);
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PositionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PositionRequest request
    ) {
        return positionService.update(id, request);
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        positionService.delete(id);
    }

    // SEARCH + PAGINATION
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/search")
    public Page<PositionResponse> search(
            @RequestParam(required = false) String keyword,

            @RequestParam(required = false)
            @DecimalMin(value = "0.0", inclusive = false)
            BigDecimal minSalary,

            @RequestParam(required = false)
            @DecimalMin(value = "0.0", inclusive = false)
            BigDecimal maxSalary,

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "5")
            @Min(1)
            int size,

            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return positionService.search(
                keyword,
                minSalary,
                maxSalary,
                page,
                size,
                sortBy,
                sortDir
        );
    }
}