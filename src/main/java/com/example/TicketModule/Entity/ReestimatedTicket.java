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
    @JoinColumn(name = "ticket")
    private Ticket ticket;

    private Long reestimatedBy;


    private Long assignedTo;
}
