package com.example.TicketModule.dto.reestimated;

import com.example.TicketModule.entity.ReestimatedTicket;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class ReestimatedTicketRequestDTO {
    private Long id;
    private Long ticketId;
    private String reason;
    private String denyReason;
    private String  status = "Pending";
    private Date newDate;
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