package com.example.Human_Resource_Management.repository;

import com.example.Human_Resource_Management.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository
        extends JpaRepository<Position, Long>,
        JpaSpecificationExecutor<Position> {

    Optional<Position> findByName(String name);
}
