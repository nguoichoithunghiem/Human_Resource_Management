package com.example.Human_Resource_Management.dto.LeaveRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaveResponse {

    private Long id;
    private String employeeName;
    private String startDate;
    private String endDate;
    private String status;
    private String reason;
}

