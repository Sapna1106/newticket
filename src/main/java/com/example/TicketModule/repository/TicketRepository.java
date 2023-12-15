package com.example.TicketModule.repository;

import com.example.TicketModule.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
//    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.projectId.id = :projectId")
//    int countTicketsByProjectId(@Param("projectId") Long projectId);

    List<Ticket> findByProjectId(Long projectId);

    List<Ticket> findByAssignee_Id(Long assigneeId);

    @Query(value = "SELECT custom_fields->>:key FROM ticketmodule.tickets y WHERE y.id = :id", nativeQuery = true)
    Optional<String> findByTicketIdAndKey(@Param("id") Long id, @Param("key") String key);



}
