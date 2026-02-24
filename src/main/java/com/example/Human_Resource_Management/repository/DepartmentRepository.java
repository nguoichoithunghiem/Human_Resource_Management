package com.example.Human_Resource_Management.repository;

import com.example.Human_Resource_Management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository
        extends JpaRepository<Department, Long>,
        JpaSpecificationExecutor<Department> {

    Optional<Department> findByName(String name);

    boolean existsByName(String name);
}
