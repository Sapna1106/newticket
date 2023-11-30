package com.example.TicketModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.TicketModule.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
