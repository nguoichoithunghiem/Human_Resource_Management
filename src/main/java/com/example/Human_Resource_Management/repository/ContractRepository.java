package com.example.Human_Resource_Management.repository;

import com.example.Human_Resource_Management.entity.Contract;
import enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ContractRepository
        extends JpaRepository<Contract, Long>,
        JpaSpecificationExecutor<Contract> {
}
