package com.example.TicketModule.DTO;
import com.example.TicketModule.Entity.CustomField;
import com.example.TicketModule.Entity.Ticket;
import com.example.TicketModule.Enum.Priority;
import com.example.TicketModule.Repository.UserRepository;
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
//    User createdBy = userRepo.findById(ticket.getCreatedBy()).get();
//    System.out.println(createdBy);
//    User accountableAssigne = userRepo.findById(ticket.getAccountableAssignee()).get();
//    System.out.println(accountableAssigne);
//    ticketResponseDto.setCreatedBy(
//        new UserDto(createdBy.getId(), createdBy.getUserName(), createdBy.getEmail()));
//    ticketResponseDto.setAccountableAssigneeName(
//        new UserDto(accountableAssigne.getId(), accountableAssigne.getUserName(), accountableAssigne.getEmail()));
//    System.out.println("done");
    return ticketResponseDto;
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