package com.example.Human_Resource_Management.security;

import com.example.Human_Resource_Management.entity.Role;
import com.example.Human_Resource_Management.entity.User;
import com.example.Human_Resource_Management.repository.RoleRepository;
import com.example.Human_Resource_Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ===== 1️⃣ CREATE ROLES IF NOT EXISTS =====
        Role adminRole = createRoleIfNotExists("ADMIN");
        Role userRole = createRoleIfNotExists("USER");
        Role managerRole = createRoleIfNotExists("MANAGER");

        // ===== 2️⃣ CREATE ADMIN =====
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRoles(Set.of(adminRole));
            admin.setEnabled(true);
            userRepository.save(admin);

            System.out.println("🔥 Admin created: admin / 123456");
        }

        // ===== 3️⃣ CREATE MANAGER =====
        if (!userRepository.existsByUsername("manager")) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("123456"));
            manager.setRoles(Set.of(managerRole));
            manager.setEnabled(true);
            userRepository.save(manager);

            System.out.println("🔥 Manager created: manager / 123456");
        }

        // ===== 4️⃣ CREATE USER =====
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            user.setEnabled(true);
            userRepository.save(user);

            System.out.println("🔥 User created: user / 123456");
        }
    }

    private Role createRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }
}
