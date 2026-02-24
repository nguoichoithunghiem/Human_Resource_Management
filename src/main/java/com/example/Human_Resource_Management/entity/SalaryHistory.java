package com.example.Human_Resource_Management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "salary_history")
@Getter
@Setter
public class SalaryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal oldSalary;
    private BigDecimal newSalary;

    private LocalDate changedDate;   // ✅ thêm field này

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
