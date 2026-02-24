package com.example.Human_Resource_Management.entity;

import com.example.Human_Resource_Management.entity.BaseEntity;
import enums.ContractStatus;
import enums.ContractType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Getter
@Setter
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
