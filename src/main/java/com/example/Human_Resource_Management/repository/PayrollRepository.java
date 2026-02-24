package com.example.Human_Resource_Management.repository;

import com.example.Human_Resource_Management.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollRepository
        extends JpaRepository<Payroll, Long>,
        JpaSpecificationExecutor<Payroll> {

    Optional<Payroll> findByEmployeeIdAndMonthAndYear(
            Long employeeId,
            int month,
            int year
    );
}
