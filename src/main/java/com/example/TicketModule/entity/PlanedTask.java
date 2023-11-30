package com.example.TicketModule.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class PlanedTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToOne
    private Ticket ticket;

    private LocalDateTime dropDate;
    private LocalDateTime startTicketTime;
    private LocalDateTime endTicketTime;
}
