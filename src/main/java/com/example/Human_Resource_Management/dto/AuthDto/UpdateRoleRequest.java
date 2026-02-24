package com.example.Human_Resource_Management.dto.AuthDto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateRoleRequest {

    private Set<String> roles;
}
