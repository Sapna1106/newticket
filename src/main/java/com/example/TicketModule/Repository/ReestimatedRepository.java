package com.example.TicketModule.Repository;

import com.example.TicketModule.Entity.ReestimatedTicket;
import com.example.TicketModule.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReestimatedRepository extends JpaRepository<ReestimatedTicket, Long> {
    List<ReestimatedTicket> findByAssignedTo(Long userId);
    ReestimatedTicket findByTicketId(Ticket ticket);
    List<ReestimatedTicket> findByStatusAndAssignedTo(String status,Long userId);
}