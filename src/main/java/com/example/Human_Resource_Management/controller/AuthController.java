package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.dto.AuthDto.LoginRequest;
import com.example.Human_Resource_Management.dto.AuthDto.LoginResponse;
import com.example.Human_Resource_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}

