package com.example.Human_Resource_Management.dto.AuthDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String username;
    private Set<String> roles;
}