package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.AuthDto.UserRequest;
import com.example.Human_Resource_Management.dto.AuthDto.UserResponse;
import com.example.Human_Resource_Management.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UserResponse create(
            @Valid @RequestBody UserRequest request
    ) {
        return userService.createUser(request);
    }

    // ADMIN + MANAGER
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public Page<UserResponse> getAll(
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return userService.getAll(
                enabled,
                role,
                employeeId,
                keyword,
                pageable
        );
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(1) Long id) {
        userService.delete(id);
    }
}