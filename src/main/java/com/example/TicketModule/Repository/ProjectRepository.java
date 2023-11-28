package com.example.TicketModule.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.TicketModule.Entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
