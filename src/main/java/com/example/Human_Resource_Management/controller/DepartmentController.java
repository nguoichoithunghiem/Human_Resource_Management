package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.DepartmentDto.DepartmentRequest;
import com.example.Human_Resource_Management.dto.DepartmentDto.DepartmentResponse;
import com.example.Human_Resource_Management.service.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public DepartmentResponse create(
            @Valid @RequestBody DepartmentRequest request
    ) {
        return departmentService.create(request);
    }

    // ADMIN + MANAGER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public List<DepartmentResponse> getAll() {
        return departmentService.getAll();
    }

    // ADMIN + MANAGER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public DepartmentResponse getById(@PathVariable Long id) {
        return departmentService.getById(id);
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public DepartmentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request
    ) {
        return departmentService.update(id, request);
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departmentService.delete(id);
    }

    // SEARCH + PAGINATION
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/search")
    public Page<DepartmentResponse> search(
            @RequestParam(required = false) String keyword,

            @RequestParam(required = false)
            @Min(value = 0, message = "minEmployees must be >= 0")
            Integer minEmployees,

            @RequestParam(required = false)
            @Min(value = 0, message = "maxEmployees must be >= 0")
            Integer maxEmployees,

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "5")
            @Min(1)
            int size,

            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return departmentService.search(
                keyword,
                minEmployees,
                maxEmployees,
                page,
                size,
                sortBy,
                sortDir
        );
    }
}