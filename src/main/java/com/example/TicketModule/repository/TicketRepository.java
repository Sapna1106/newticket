package com.example.TicketModule.repository;

import com.example.TicketModule.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
//    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.projectId.id = :projectId")
//    int countTicketsByProjectId(@Param("projectId") Long projectId);

    List<Ticket> findByProjectId(Long projectId);

    List<Ticket> findByAssignee_Id(Long assigneeId);
}
