package com.example.Human_Resource_Management.dto.AuthDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private boolean enabled;
    private Set<String> roles;
}
