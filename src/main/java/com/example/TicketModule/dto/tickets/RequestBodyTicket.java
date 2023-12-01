package com.example.TicketModule.dto.tickets;

import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.enums.Priority;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
public class RequestBodyTicket {
  private Long id;
  private String ticketId;
  private String name;
  private String description;
  private Date startDate;
  private Date endDate;
  private Date endTime;
  private Long createdBy;
  private Long projectId;
  private String status;
  private String priority;

  private Long stageId;
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
    ticket.setStageId(newTicket.getStageId());
    return ticket;
  }
}
