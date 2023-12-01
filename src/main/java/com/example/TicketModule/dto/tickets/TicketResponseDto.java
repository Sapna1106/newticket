package com.example.TicketModule.dto.tickets;

import com.example.TicketModule.entity.CustomField;
import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.enums.Priority;
import com.example.TicketModule.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TicketResponseDto {
  private Long id;
  private String ticketId;
  private String name;
  private String description;
  private UserDto createdBy;

  private Date startDate;
  private Date endDate;
  private Date endTime;

  private ProjectDto project;

  private String status ;
  private String priority ;
  private List<UserDto> assigneeName = new ArrayList<>();

  private UserDto accountableAssigneeName;

  private String customFields;

  private List<CustomField> customFieldList;

  private Long stageId;

  @Autowired private UserRepository userRepo;



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
    this.stageId = ticket.getStageId();
  }
}
