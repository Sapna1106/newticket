package com.example.TicketModule.Repository;

import com.example.TicketModule.Entity.ReestimatedTicket;
import com.example.TicketModule.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReestimatedRepository extends JpaRepository<ReestimatedTicket, Long> {
//    Optional<List<ReestimatedTicket>> findByAssignedTo(Long userId);
//    Optional<ReestimatedTicket> findByAssignedTo(Long assignedTo);
//    Optional<List<ReestimatedTicket>> findByAssignedToAndStatus(Long assignedToId, String status);
    @Query("SELECT rt FROM ReestimatedTicket rt WHERE rt.status = :status AND rt.assignedTo.id = :userId")
    List<ReestimatedTicket> findByStatusAndAssignedToId(@Param("status") String status, @Param("userId") Long userId);
}
