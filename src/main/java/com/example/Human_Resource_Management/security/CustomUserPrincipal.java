package com.example.Human_Resource_Management.security;

import com.example.Human_Resource_Management.entity.Role;
import com.example.Human_Resource_Management.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CustomUserPrincipal implements UserDetails {

    private final Long userId;
    private final Long employeeId;
    private final Long departmentId;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Set<Role> roles;

    public CustomUserPrincipal(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.roles = user.getRoles();

        this.employeeId = user.getEmployee() != null
                ? user.getEmployee().getId()
                : null;

        this.departmentId = user.getEmployee() != null
                ? user.getEmployee().getDepartment().getId()
                : null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}