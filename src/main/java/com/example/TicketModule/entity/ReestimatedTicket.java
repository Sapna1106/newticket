package com.example.TicketModule.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "reestimatedTickets")
public class ReestimatedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date newDate;
    private String reason;
    private String denyReason;
    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Long reestimatedBy;


    private Long assignedTo;
}
