package com.example.Human_Resource_Management.dto.AttendanceDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceRequest {

    private Long employeeId;
    private String checkIn;
    private String checkOut;
}

