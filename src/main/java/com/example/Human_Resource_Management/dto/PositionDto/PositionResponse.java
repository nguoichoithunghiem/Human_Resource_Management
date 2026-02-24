package com.example.Human_Resource_Management.dto.PositionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PositionResponse {

    private Long id;
    private String name;
    private BigDecimal baseSalary;
}
