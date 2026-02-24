package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.ContractDto.ContractRequest;
import com.example.Human_Resource_Management.dto.ContractDto.ContractResponse;
import com.example.Human_Resource_Management.service.ContractService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@Validated
public class ContractController {

    private final ContractService contractService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ContractResponse create(
            @Valid @RequestBody ContractRequest request
    ) {
        return contractService.create(request);
    }

    // ✅ ADMIN + MANAGER + USER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping
    public Page<ContractResponse> getAll(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return contractService.getAll(
                employeeId,
                status,
                contractType,
                fromDate,
                toDate,
                keyword,
                pageable
        );
    }
}


