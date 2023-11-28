package com.example.TicketModule.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "reestimatedTickets")
public class ReestimatedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant newDate;
    private String reason;
    private String denyReason;
    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticketId;

    @ManyToOne
    @JoinColumn(name = "reestimated_by_id")
    private User reestimatedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;
}
