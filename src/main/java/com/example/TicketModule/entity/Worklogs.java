package com.example.TicketModule.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Worklogs {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)@Column(name ="log_id",nullable=false ,columnDefinition = "SERIAL")Long log_id;

    private Long userId;

    private Long ticketId;


    private LocalDateTime date;
    private LocalDateTime start_time;//min
    private LocalDateTime end_time; //min
    private  int duration; //min


}
