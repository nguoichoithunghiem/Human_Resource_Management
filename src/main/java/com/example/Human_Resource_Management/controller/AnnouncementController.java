package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.AnnouncementDto.AnnouncementRequest;
import com.example.Human_Resource_Management.dto.AnnouncementDto.AnnouncementResponse;
import com.example.Human_Resource_Management.service.AnnouncementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Validated
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // ✅ ADMIN + MANAGER tạo thông báo
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public AnnouncementResponse create(
            @Valid @RequestBody AnnouncementRequest request
    ) {
        return announcementService.create(request);
    }

    // ✅ Tất cả authenticated user được xem
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<AnnouncementResponse> getAll() {
        return announcementService.getAll();
    }

    // ✅ ADMIN + MANAGER + USER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping("/employee/{employeeId}")
    public List<AnnouncementResponse> getByEmployee(
            @PathVariable
            @Positive(message = "Employee id must be positive")
            Long employeeId
    ) {
        return announcementService.getByEmployee(employeeId);
    }
}