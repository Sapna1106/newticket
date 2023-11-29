package com.example.TicketModule.DTO;

import com.example.TicketModule.Entity.ReestimatedTicket;
import lombok.Data;

import java.time.Instant;

@Data
public class ReestimatedTicketRequestDTO {
    private Long id;
    private Long ticketId;
    private String reason;
    private String denyReason;
    private String  status = "Pending";
    private Instant newDate;
    private Long reestimatedBy;
    private Long assignedTo;

    public ReestimatedTicket convertToEntity(ReestimatedTicketRequestDTO newTicket) {
        ReestimatedTicket ticket = new ReestimatedTicket();
        ticket.setStatus(newTicket.getStatus());
        ticket.setReason(newTicket.getReason());
        ticket.setNewDate(newTicket.getNewDate());
//        ticket.setTicketId(newTicket.getTicketId());
        ticket.setDenyReason(newTicket.getDenyReason());
        ticket.setReestimatedBy(newTicket.getReestimatedBy());
        ticket.setAssignedTo(newTicket.getAssignedTo());
        return ticket;
    }
}