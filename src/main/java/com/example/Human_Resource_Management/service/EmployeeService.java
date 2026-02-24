package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.EmployeeDto.EmployeeRequest;
import com.example.Human_Resource_Management.dto.EmployeeDto.EmployeeResponse;
import com.example.Human_Resource_Management.entity.Department;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.entity.Position;
import com.example.Human_Resource_Management.repository.DepartmentRepository;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import com.example.Human_Resource_Management.repository.PositionRepository;
import com.example.Human_Resource_Management.security.CustomUserPrincipal;
import com.example.Human_Resource_Management.specification.EmployeeSpecification;
import enums.EmployeeStatus;
import enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    // ==============================
    // Mapping
    // ==============================

    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getGender() != null ? employee.getGender().name() : null,
                employee.getDateOfBirth(),
                employee.getHireDate(),
                employee.getStatus() != null ? employee.getStatus().name() : null,
                employee.getDepartment() != null ? employee.getDepartment().getName() : null,
                employee.getPosition() != null ? employee.getPosition().getName() : null
        );
    }

    // ==============================
    // CRUD (ADMIN only via Controller)
    // ==============================

    public EmployeeResponse create(EmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setHireDate(request.getHireDate());

        try {
            employee.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid gender value");
        }

        try {
            employee.setStatus(EmployeeStatus.valueOf(request.getStatus().toUpperCase()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid status value");
        }

        employee.setDepartment(department);
        employee.setPosition(position);

        employeeRepository.save(employee);

        return mapToResponse(employee);
    }

    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setHireDate(request.getHireDate());

        employee.setGender(request.getGender() != null
                ? Gender.valueOf(request.getGender())
                : null);

        employee.setStatus(request.getStatus() != null
                ? EmployeeStatus.valueOf(request.getStatus())
                : null);

        employee.setDepartment(department);
        employee.setPosition(position);

        employeeRepository.save(employee);

        return mapToResponse(employee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    // ==============================
    // SECURE GET BY ID
    // ==============================

    public EmployeeResponse getByIdWithSecurity(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // ADMIN → xem tất cả
        if (user.hasRole("ADMIN")) {
            return mapToResponse(employee);
        }

        // MANAGER → chỉ phòng ban mình
        if (user.hasRole("MANAGER")) {
            if (!employee.getDepartment().getId().equals(user.getDepartmentId())) {
                throw new AccessDeniedException("Access denied");
            }
            return mapToResponse(employee);
        }

        // USER → chỉ xem chính mình
        if (user.hasRole("USER")) {
            if (!employee.getId().equals(user.getEmployeeId())) {
                throw new AccessDeniedException("Access denied");
            }
            return mapToResponse(employee);
        }

        throw new AccessDeniedException("Access denied");
    }

    // ==============================
    // SECURE SEARCH
    // ==============================

    public Page<EmployeeResponse> searchWithSecurity(
            String keyword,
            String status,
            Long departmentId,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();

        if (user.hasRole("USER")) {
            throw new AccessDeniedException("Users cannot search employees");
        }

        if (user.hasRole("MANAGER")) {
            departmentId = user.getDepartmentId();
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        EmployeeStatus employeeStatus = null;

        if (status != null && !status.isBlank()) {
            employeeStatus = EmployeeStatus.valueOf(status);
        }

        Specification<Employee> spec =
                EmployeeSpecification.searchAndFilter(keyword, employeeStatus, departmentId);

        Page<Employee> result = employeeRepository.findAll(spec, pageable);

        return result.map(this::mapToResponse);
    }
}