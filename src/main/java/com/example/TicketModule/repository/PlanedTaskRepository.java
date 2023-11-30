package com.example.TicketModule.repository;

import com.example.TicketModule.entity.PlanedTask;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanedTaskRepository extends JpaRepository<PlanedTask, Long> {

    PlanedTask findByUserIdAndTicketId(Long userId, Long ticketId);

    @Modifying
    @Query("DELETE FROM PlanedTask ut WHERE ut.userId = :userId AND ut.ticket.id = :ticketId")
    void deleteFromUserTicketByUserIdAndTicketId(@Param("userId") Long userId, @Param("ticketId") Long ticketId);

//    @Query("select r FROM PlanedTask ut WHERE ut.userId = :userId AND ut.ticket.id = :ticketId")
//    PlanedTask findFromUserTicketByUserIdAndTicketId(@Param("userId") Long userId, @Param("ticketId") Long ticketId);
    List<PlanedTask> findByUserId(Long userId);
}
