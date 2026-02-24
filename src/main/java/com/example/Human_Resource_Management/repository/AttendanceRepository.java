package com.example.Human_Resource_Management.repository;

import com.example.Human_Resource_Management.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface AttendanceRepository
        extends JpaRepository<Attendance, Long>,
        JpaSpecificationExecutor<Attendance> {
}
