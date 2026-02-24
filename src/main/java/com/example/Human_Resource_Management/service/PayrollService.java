package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.PayrollDto.PayrollRequest;
import com.example.Human_Resource_Management.dto.PayrollDto.PayrollResponse;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.entity.Payroll;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import com.example.Human_Resource_Management.repository.PayrollRepository;
import com.example.Human_Resource_Management.specification.PayrollSpecification;
import enums.PayrollStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    // ✅ Calculate Payroll
    public PayrollResponse calculate(PayrollRequest request) {

        // Check duplicate payroll
        if (payrollRepository
                .findByEmployeeIdAndMonthAndYear(
                        request.getEmployeeId(),
                        request.getMonth(),
                        request.getYear()
                ).isPresent()) {
            throw new RuntimeException("Payroll already exists for this month");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        BigDecimal baseSalary = employee.getPosition().getBaseSalary();

        BigDecimal bonus = request.getBonus() != null
                ? request.getBonus()
                : BigDecimal.ZERO;

        BigDecimal deduction = request.getDeduction() != null
                ? request.getDeduction()
                : BigDecimal.ZERO;

        BigDecimal netSalary = baseSalary.add(bonus).subtract(deduction);

        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setMonth(request.getMonth());
        payroll.setYear(request.getYear());
        payroll.setBaseSalary(baseSalary);
        payroll.setBonus(bonus);
        payroll.setDeduction(deduction);
        payroll.setNetSalary(netSalary);
        payroll.setStatus(PayrollStatus.PENDING);

        payrollRepository.save(payroll);

        return mapToResponse(payroll);
    }

    // ✅ Pagination getAll
    public Page<PayrollResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return payrollRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    private PayrollResponse mapToResponse(Payroll payroll) {
        return new PayrollResponse(
                payroll.getId(),
                payroll.getEmployee().getFirstName() + " " +
                        payroll.getEmployee().getLastName(),
                payroll.getMonth(),
                payroll.getYear(),
                payroll.getBaseSalary(),
                payroll.getBonus(),
                payroll.getDeduction(),
                payroll.getNetSalary(),
                payroll.getStatus().name()
        );
    }

    // ✅ Search + Filter
    public Page<PayrollResponse> search(
            String keyword,
            String status,
            Integer month,
            Integer year,
            Long employeeId,
            BigDecimal minNetSalary,
            BigDecimal maxNetSalary,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {

        if (minNetSalary != null && maxNetSalary != null
                && minNetSalary.compareTo(maxNetSalary) > 0) {
            throw new RuntimeException("minNetSalary cannot be greater than maxNetSalary");
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PayrollStatus payrollStatus = null;
        if (status != null) {
            try {
                payrollStatus = PayrollStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid payroll status");
            }
        }

        Specification<Payroll> spec =
                PayrollSpecification.searchAndFilter(
                        keyword,
                        payrollStatus,
                        month,
                        year,
                        employeeId,
                        minNetSalary,
                        maxNetSalary
                );

        return payrollRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }
}