package com.example.Human_Resource_Management.repository;


import com.example.Human_Resource_Management.entity.SalaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface SalaryHistoryRepository
        extends JpaRepository<SalaryHistory, Long>,
        JpaSpecificationExecutor<SalaryHistory> {
}
