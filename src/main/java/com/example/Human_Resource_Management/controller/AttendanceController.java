package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.AttendanceDto.AttendanceRequest;
import com.example.Human_Resource_Management.dto.AttendanceDto.AttendanceResponse;
import com.example.Human_Resource_Management.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // USER check-in chính mình
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @PostMapping("/check-in")
    public AttendanceResponse checkIn(@RequestBody AttendanceRequest request) {
        return attendanceService.checkInWithSecurity(request);
    }

    // USER check-out chính mình
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @PutMapping("/check-out/{attendanceId}")
    public AttendanceResponse checkOut(@PathVariable Long attendanceId) {
        return attendanceService.checkOutWithSecurity(attendanceId);
    }

    // ADMIN & MANAGER search
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping
    public Page<AttendanceResponse> getAll(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return attendanceService.getAllWithSecurity(
                employeeId,
                status,
                fromDate,
                toDate,
                keyword,
                pageable
        );
    }
}

