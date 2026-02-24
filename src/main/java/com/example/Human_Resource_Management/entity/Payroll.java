package com.example.Human_Resource_Management.entity;

import com.example.Human_Resource_Management.entity.BaseEntity;
import enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payroll")
@Getter
@Setter
public class Payroll extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int month;
    private int year;

    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal deduction;
    private BigDecimal netSalary;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
