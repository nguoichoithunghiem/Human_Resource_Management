package com.example.Human_Resource_Management.dto.PayrollDto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayrollRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be positive")
    private Long employeeId;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private int month;

    @Min(value = 2000, message = "Year must be valid")
    @Max(value = 2100, message = "Year must be valid")
    private int year;

    @DecimalMin(value = "0.0", inclusive = true,
            message = "Bonus must be >= 0")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal bonus;

    @DecimalMin(value = "0.0", inclusive = true,
            message = "Deduction must be >= 0")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal deduction;
}