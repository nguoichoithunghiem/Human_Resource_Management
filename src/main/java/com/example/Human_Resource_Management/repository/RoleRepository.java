package com.example.Human_Resource_Management.repository;

import com.example.Human_Resource_Management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository
        extends JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {

    Optional<Role> findByName(String name);   // ✅ THÊM DÒNG NÀY
}
