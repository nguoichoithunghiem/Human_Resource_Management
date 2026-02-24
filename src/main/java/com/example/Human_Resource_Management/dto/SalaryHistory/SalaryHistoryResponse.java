package com.example.Human_Resource_Management.dto.SalaryHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SalaryHistoryResponse {

    private Long id;
    private String employeeName;
    private Double oldSalary;
    private Double newSalary;
    private String changedDate;
}
