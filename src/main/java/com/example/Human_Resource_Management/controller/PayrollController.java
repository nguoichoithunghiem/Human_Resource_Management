package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.PayrollDto.PayrollRequest;
import com.example.Human_Resource_Management.dto.PayrollDto.PayrollResponse;
import com.example.Human_Resource_Management.service.PayrollService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@Validated
public class PayrollController {

    private final PayrollService payrollService;

    // ✅ ADMIN – Calculate Payroll
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PayrollResponse calculate(
            @Valid @RequestBody PayrollRequest request
    ) {
        return payrollService.calculate(request);
    }

    // ✅ ADMIN + MANAGER + USER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping
    public Page<PayrollResponse> getAll(

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "5")
            @Min(1)
            int size
    ) {
        return payrollService.getAll(page, size);
    }


    // ✅ SEARCH
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping("/search")
    public Page<PayrollResponse> search(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false)
            @Pattern(regexp = "PENDING|PAID|CANCELLED",
                    message = "Invalid status")
            String status,

            @RequestParam(required = false)
            @Min(1) @Max(12)
            Integer month,

            @RequestParam(required = false)
            @Min(2000) @Max(2100)
            Integer year,

            @RequestParam(required = false)
            @Positive
            Long employeeId,

            @RequestParam(required = false)
            @DecimalMin(value = "0.0", inclusive = true)
            BigDecimal minNetSalary,

            @RequestParam(required = false)
            @DecimalMin(value = "0.0", inclusive = true)
            BigDecimal maxNetSalary,

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "5")
            @Min(1)
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            @Pattern(regexp = "asc|desc",
                    message = "sortDir must be asc or desc")
            String sortDir
    ) {
        return payrollService.search(
                keyword,
                status,
                month,
                year,
                employeeId,
                minNetSalary,
                maxNetSalary,
                page,
                size,
                sortBy,
                sortDir
        );
    }
}
