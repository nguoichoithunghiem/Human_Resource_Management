package com.example.Human_Resource_Management.dto.ContractDto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContractRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be positive")
    private Long employeeId;

    @NotBlank(message = "Contract type is required")
    private String contractType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or future")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Salary must be greater than 0")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal salary;
}