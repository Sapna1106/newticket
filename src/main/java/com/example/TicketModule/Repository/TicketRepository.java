package com.example.TicketModule.Repository;

import com.example.TicketModule.Entity.Project;
import com.example.TicketModule.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
//    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.projectId.id = :projectId")
//    int countTicketsByProjectId(@Param("projectId") Long projectId);

    List<Ticket> findByProjectId(Long projectId);

    List<Ticket> findByAssignee_Id(Long assigneeId);
}
