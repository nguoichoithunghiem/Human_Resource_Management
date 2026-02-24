package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.SalaryHistory.SalaryHistoryRequest;
import com.example.Human_Resource_Management.dto.SalaryHistory.SalaryHistoryResponse;
import com.example.Human_Resource_Management.service.SalaryHistoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/salary-history")
@RequiredArgsConstructor
@Validated
public class SalaryHistoryController {

    private final SalaryHistoryService salaryHistoryService;

    // ✅ ADMIN ONLY – create salary history
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SalaryHistoryResponse create(
            @Valid @RequestBody SalaryHistoryRequest request
    ) {
        return salaryHistoryService.create(request);
    }

    // ✅ ADMIN + MANAGER + USER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping
    public Page<SalaryHistoryResponse> getAll(

            @RequestParam(required = false)
            @Min(value = 1, message = "Employee ID must be greater than 0")
            Long employeeId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(required = false)
            String keyword,

            Pageable pageable
    ) {
        return salaryHistoryService.getAll(
                employeeId,
                fromDate,
                toDate,
                keyword,
                pageable
        );
    }
}