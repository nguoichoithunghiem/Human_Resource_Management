package com.example.Human_Resource_Management.dto.ContractDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContractResponse {

    private Long id;
    private String employeeName;
    private String contractType;
    private String startDate;
    private String endDate;
    private String status;
}

