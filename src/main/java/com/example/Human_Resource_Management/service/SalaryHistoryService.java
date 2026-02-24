package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.SalaryHistory.SalaryHistoryRequest;
import com.example.Human_Resource_Management.dto.SalaryHistory.SalaryHistoryResponse;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.entity.SalaryHistory;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import com.example.Human_Resource_Management.repository.SalaryHistoryRepository;
import com.example.Human_Resource_Management.specification.SalaryHistorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SalaryHistoryService {

    private final SalaryHistoryRepository salaryHistoryRepository;
    private final EmployeeRepository employeeRepository;

    public SalaryHistoryResponse create(SalaryHistoryRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Business validation
        if (request.getOldSalary().compareTo(request.getNewSalary()) == 0) {
            throw new IllegalArgumentException("New salary must be different from old salary");
        }

        SalaryHistory history = new SalaryHistory();
        history.setEmployee(employee);
        history.setOldSalary(request.getOldSalary());
        history.setNewSalary(request.getNewSalary());
        history.setChangedDate(LocalDate.now());

        salaryHistoryRepository.save(history);

        return mapToResponse(history);
    }

    public Page<SalaryHistoryResponse> getAll(
            Long employeeId,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword,
            Pageable pageable
    ) {

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date must be before to date");
        }

        Specification<SalaryHistory> spec =
                SalaryHistorySpecification.filter(employeeId, fromDate, toDate, keyword);

        return salaryHistoryRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    private SalaryHistoryResponse mapToResponse(SalaryHistory history) {
        return new SalaryHistoryResponse(
                history.getId(),
                history.getEmployee().getFirstName() + " " +
                        history.getEmployee().getLastName(),
                history.getOldSalary().doubleValue(),
                history.getNewSalary().doubleValue(),
                history.getChangedDate().toString()
        );
    }
}