package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.AuthDto.LoginRequest;
import com.example.Human_Resource_Management.dto.AuthDto.LoginResponse;
import com.example.Human_Resource_Management.dto.AuthDto.UserRequest;
import com.example.Human_Resource_Management.dto.AuthDto.UserResponse;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.entity.Role;
import com.example.Human_Resource_Management.entity.User;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import com.example.Human_Resource_Management.repository.RoleRepository;
import com.example.Human_Resource_Management.repository.UserRepository;
import com.example.Human_Resource_Management.security.JwtService;
import com.example.Human_Resource_Management.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // ================= CREATE =================

    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Normalize role names (uppercase)
        Set<Role> roleEntities = request.getRoles().stream()
                .map(String::toUpperCase)
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmployee(employee);
        user.setRoles(roleEntities);
        user.setEnabled(true);

        userRepository.save(user);

        return mapToResponse(user);
    }

    // ================= LOGIN =================

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        String token = jwtService.generateToken(user);

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new LoginResponse(
                token,
                user.getUsername(),
                roles
        );
    }

    // ================= SEARCH =================

    public Page<UserResponse> getAll(
            Boolean enabled,
            String role,
            Long employeeId,
            String keyword,
            Pageable pageable
    ) {

        Specification<User> spec =
                UserSpecification.filter(enabled, role, employeeId, keyword);

        return userRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    // ================= DELETE =================

    @Transactional
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmployee() != null) {
            user.getEmployee().setUser(null);
            user.setEmployee(null);
        }

        userRepository.delete(user);
    }

    // ================= MAPPER =================

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.isEnabled(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }
}