package com.example.Human_Resource_Management.dto.AuthDto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Employee is required")
    private Long employeeId;

    @NotEmpty(message = "At least one role is required")
    private Set<@NotBlank(message = "Role name cannot be blank") String> roles;
}