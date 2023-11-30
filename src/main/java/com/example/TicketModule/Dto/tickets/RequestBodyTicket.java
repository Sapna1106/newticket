package com.example.TicketModule.Dto.tickets;

import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.enums.Priority;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class RequestBodyTicket {
  private Long id;
  private String ticketId;
  private String name;
  private String description;
  private Instant startDate;
  private Instant endDate;
  private Instant endTime;
  private Long createdBy;
  private Long projectId;
  private String status;
  private Priority priority;

  private List<Long> assignee;
  private Long accountableAssignee;
  private String customFields;

  public Ticket convertToEntity(RequestBodyTicket newTicket) {
    Ticket ticket = new Ticket();
    ticket.setProjectId(newTicket.getProjectId());
    ticket.setCreatedBy(newTicket.getCreatedBy());
    ticket.setAccountableAssignee(newTicket.getAccountableAssignee());
    ticket.setTicketId(newTicket.getTicketId());
    ticket.setId(newTicket.getId());
    ticket.setName(newTicket.getName());
    ticket.setDescription(newTicket.getDescription());
    ticket.setStartDate(newTicket.getStartDate());
    ticket.setEndDate(newTicket.getEndDate());
    ticket.setEndTime(newTicket.getEndTime());
    ticket.setStatus(newTicket.getStatus());
    ticket.setPriority(newTicket.getPriority());
    ticket.setCustomFields(newTicket.getCustomFields());
    return ticket;
  }
}
