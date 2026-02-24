package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.DepartmentDto.DepartmentRequest;
import com.example.Human_Resource_Management.dto.DepartmentDto.DepartmentResponse;
import com.example.Human_Resource_Management.entity.Department;
import com.example.Human_Resource_Management.repository.DepartmentRepository;
import com.example.Human_Resource_Management.specification.DepartmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private DepartmentResponse mapToResponse(Department d) {
        return new DepartmentResponse(
                d.getId(),
                d.getName(),
                d.getDescription()
        );
    }

    // ================= CREATE =================

    public DepartmentResponse create(DepartmentRequest request) {

        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department name already exists");
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());

        departmentRepository.save(department);

        return mapToResponse(department);
    }

    // ================= READ =================

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return mapToResponse(department);
    }

    // ================= UPDATE =================

    public DepartmentResponse update(Long id, DepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        departmentRepository.findByName(request.getName())
                .filter(d -> !d.getId().equals(id))
                .ifPresent(d -> {
                    throw new RuntimeException("Department name already exists");
                });

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        departmentRepository.save(department);

        return mapToResponse(department);
    }

    // ================= DELETE =================

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }

    // ================= SEARCH =================

    public Page<DepartmentResponse> search(
            String keyword,
            Integer minEmployees,
            Integer maxEmployees,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {

        if (minEmployees != null && maxEmployees != null
                && minEmployees > maxEmployees) {
            throw new IllegalArgumentException("minEmployees cannot be greater than maxEmployees");
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Department> spec =
                DepartmentSpecification.searchAndFilter(keyword, minEmployees, maxEmployees);

        Page<Department> result = departmentRepository.findAll(spec, pageable);

        return result.map(this::mapToResponse);
    }
}