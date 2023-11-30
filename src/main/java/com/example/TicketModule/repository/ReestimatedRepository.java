package com.example.TicketModule.repository;

import com.example.TicketModule.entity.ReestimatedTicket;
import com.example.TicketModule.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReestimatedRepository extends JpaRepository<ReestimatedTicket, Long> {
    List<ReestimatedTicket> findByAssignedTo(Long userId);
    ReestimatedTicket findByTicketId(Ticket ticket);
    List<ReestimatedTicket> findByStatusAndAssignedTo(String status,Long userId);
}