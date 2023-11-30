package com.example.TicketModule.Dto.tickets;

import com.example.TicketModule.entity.CustomField;
import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.enums.Priority;
import com.example.TicketModule.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class TicketResponseDto {
  private Long id;
  private String ticketId;
  private String name;
  private String description;
  private UserDto createdBy;

  private Instant startDate;
  private Instant endDate;
  private Instant endTime;

  private ProjectDto project;

  private String status = "To Do";
  private Priority priority = Priority.MEDIUM;
  private List<UserDto> assigneeName = new ArrayList<>();

  private UserDto accountableAssigneeName;

  private String customFields;

  private List<CustomField> customFieldList;

  @Autowired private UserRepository userRepo;

  public TicketResponseDto convertToDtos(Ticket ticket, TicketResponseDto ticketResponseDto) {
    System.out.println("Hello");
    ticketResponseDto.setId(ticket.getId());
    ticketResponseDto.setName(ticket.getName());
    ticketResponseDto.setTicketId(ticket.getTicketId());
    ticketResponseDto.setDescription(ticket.getDescription());
    ticketResponseDto.setStartDate(ticket.getStartDate());
    ticketResponseDto.setEndDate(ticket.getEndDate());
    ticketResponseDto.setEndTime(ticket.getEndTime());
    ticketResponseDto.setStatus(ticket.getStatus());
    ticketResponseDto.setPriority(ticket.getPriority());
    System.out.println(ticket);
    return ticketResponseDto;
  }

  public TicketResponseDto(){

  }

  public TicketResponseDto(Ticket ticket){
    this.id = ticket.getId();
    this.name = ticket.getName();
    this.ticketId = ticket.getTicketId();
    this.description = ticket.getDescription();
    this.startDate = ticket.getStartDate();
    this.endDate = ticket.getEndDate();
    this.endTime = ticket.getEndTime();
    this.status = ticket.getStatus();
    this.priority = ticket.getPriority();
    this.customFields = ticket.getCustomFields();
  }
}
