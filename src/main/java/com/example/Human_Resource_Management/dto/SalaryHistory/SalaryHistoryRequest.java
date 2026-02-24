package com.example.Human_Resource_Management.dto.SalaryHistory;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SalaryHistoryRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be greater than 0")
    private Long employeeId;

    @NotNull(message = "Old salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Old salary must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Invalid salary format")
    private BigDecimal oldSalary;

    @NotNull(message = "New salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "New salary must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Invalid salary format")
    private BigDecimal newSalary;
}