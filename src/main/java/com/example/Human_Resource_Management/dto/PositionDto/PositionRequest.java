package com.example.Human_Resource_Management.dto.PositionDto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PositionRequest {

    @NotBlank(message = "Position name is required")
    @Size(max = 100, message = "Position name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base salary must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Invalid salary format")
    private BigDecimal baseSalary;
}