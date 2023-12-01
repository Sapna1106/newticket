package com.example.TicketModule.repository;

import com.example.TicketModule.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageRepo extends JpaRepository<Stage, Long> {
    public List<Stage> findByProjectId(Long projectId);

    Optional<Stage> findByIdAndProjectId(Long id,Long projectId);
}