package com.example.TicketModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorklogTicketDto {
    private Long ticketId;
    private String ticketName;
}
