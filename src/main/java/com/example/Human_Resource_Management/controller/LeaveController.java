package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.LeaveRequestDTO.LeaveRequestDTO;
import com.example.Human_Resource_Management.dto.LeaveRequestDTO.LeaveResponse;
import com.example.Human_Resource_Management.service.LeaveService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Validated
public class LeaveController {

    private final LeaveService leaveService;

    // USER tạo đơn
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public LeaveResponse requestLeave(
            @Valid @RequestBody LeaveRequestDTO requestDTO
    ) {
        return leaveService.requestLeave(requestDTO);
    }

    // Xem danh sách
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping
    public Page<LeaveResponse> getAll(

            @RequestParam(required = false)
            @Positive
            Long employeeId,

            @RequestParam(required = false)
            @Pattern(regexp = "PENDING|APPROVED|REJECTED",
                    message = "Invalid leave status")
            String status,

            @RequestParam(required = false)
            String keyword,

            Pageable pageable
    ) {
        return leaveService.getAll(employeeId, status, keyword, pageable);
    }
}