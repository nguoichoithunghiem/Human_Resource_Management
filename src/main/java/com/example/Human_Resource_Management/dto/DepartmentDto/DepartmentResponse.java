package com.example.Human_Resource_Management.dto.DepartmentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentResponse {

    private Long id;
    private String name;
    private String description;
}