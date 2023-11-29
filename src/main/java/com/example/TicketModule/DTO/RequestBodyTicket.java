package com.example.TicketModule.DTO;
import com.example.TicketModule.Entity.Project;
import com.example.TicketModule.Entity.Ticket;
import com.example.TicketModule.Entity.User;
import com.example.TicketModule.Enum.Priority;
import lombok.Data;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    System.out.println(newTicket.getCustomFields());
    ticket.setCustomFields(newTicket.getCustomFields());
    return ticket;
  }
}