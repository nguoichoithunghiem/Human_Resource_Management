package com.example.Human_Resource_Management.repository;

import com.example.Human_Resource_Management.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByCreatedById(Long employeeId);

    List<Announcement> findAllByOrderByCreatedAtDesc();
}