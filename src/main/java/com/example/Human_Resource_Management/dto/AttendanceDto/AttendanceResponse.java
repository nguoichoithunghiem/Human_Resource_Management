package com.example.Human_Resource_Management.dto.AttendanceDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;
    private String employeeName;
    private String checkIn;
    private String checkOut;
    private String status;
}

