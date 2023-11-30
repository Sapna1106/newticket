package com.example.TicketModule.Service;

import com.example.TicketModule.Controller.TicketController;
import com.example.TicketModule.DTO.ProjectDto;
import com.example.TicketModule.DTO.RequestBodyTicket;
import com.example.TicketModule.DTO.TicketResponseDto;
import com.example.TicketModule.DTO.UserDto;
import com.example.TicketModule.Entity.CustomField;
import com.example.TicketModule.Entity.Project;
import com.example.TicketModule.Entity.Ticket;
import com.example.TicketModule.Entity.User;
import com.example.TicketModule.Exception.ProjectNotFoundException;
import com.example.TicketModule.Exception.TicketNotFoundException;
import com.example.TicketModule.Exception.UserNotFoundException;
import com.example.TicketModule.Repository.CustomFieldRepository;
import com.example.TicketModule.Repository.ProjectRepository;
import com.example.TicketModule.Repository.TicketRepository;
import com.example.TicketModule.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

  private static final Logger log = LoggerFactory.getLogger(TicketController.class);
  @Autowired private TicketRepository ticketRepository;
  @Autowired private ProjectRepository projectRepo;
  @Autowired private UserRepository userRepo;
  @Autowired private ModelMapper modelMapper;

  @Autowired private CustomFieldRepository customizedRepository;

  //  public TicketResponseDto createTicket(RequestBodyTicket newTicket) {
  //    log.info("ticket" + newTicket);
  //    Ticket ticket = newTicket.convertToEntity(newTicket);
  //    log.info("rrr");
  //    System.out.println(newTicket.getProjectId());
  //    Project project = projectRepo.findById(newTicket.getProjectId()).orElse(null);
  //    System.out.println(project);
  //    try {
  //      log.info("inside try ");
  //      if (project != null) {
  //        log.info("project is not null");
  //
  //        List<User> assignees = new ArrayList<>();
  //        for (Long userId : newTicket.getAssignee()) {
  //          assignees.add(userRepo.findById(userId).get());
  //        }
  //        ticket.setAssignee(assignees);
  //        ticket = ticketRepository.save(ticket);
  //        TicketResponseDto ticketResponseDto = new TicketResponseDto();
  //        ticketResponseDto = ticketResponseDto.convertToDtos(ticket, ticketResponseDto);
  //        convertTouser(ticket,ticketResponseDto);
  //        log.info("create servce Completed");
  //        return ticketResponseDto;
  //      } else {
  //        log.info("else  " + project.getInitials() + "-1");
  //        return null;
  //      }
  //
  //    } catch (Exception e) {
  //
  //      throw new TicketCreationException("Failed to create ticket. Please try again later.");
  //    }
  //  }

  public TicketResponseDto createTicket(RequestBodyTicket newTicket) {
    log.info("TicketService : createTicket Execution started"+newTicket);
    try {
      Ticket ticket = newTicket.convertToEntity(newTicket);
      Project project = projectRepo.findById(newTicket.getProjectId()).orElse(null);
      if (project != null) {
        log.info("TicketService: project is not null");
        List<Ticket> ticketsForProject = ticketRepository.findByProjectId(newTicket.getProjectId());

        if (!ticketsForProject.isEmpty()) {
          log.info("TicketService: list is not empty");
          Ticket lastTicket = ticketsForProject.get(ticketsForProject.size() - 1);
          String lastCustomId = lastTicket.getTicketId();
          String projectInitials = lastCustomId.split("-")[0];
          int lastTicketNumber = Integer.parseInt(lastCustomId.split("-")[1]);
          int newTicketNumber = lastTicketNumber + 1;
          ticket.setTicketId(projectInitials + "-" + newTicketNumber);
          List<User> assignees = new ArrayList<>();
          for (Long userId : newTicket.getAssignee()) {
            User user = userRepo.findById(userId).orElse(null);
            if(user != null)
              assignees.add(user);
          }
          ticket.setAssignee(assignees);
          ticket = ticketRepository.save(ticket);
          TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);
          convertToUser(ticket, ticketResponseDto);
          log.info("TicketService : createTicket Execution End");
          return ticketResponseDto;

        } else {
          ticket.setTicketId(project.getInitials().toUpperCase() + "-1");
          log.info("TicketService: list is  empty");
          List<User> assignees = new ArrayList<>();
          for (Long userId : newTicket.getAssignee()) {
            assignees.add(userRepo.findById(userId).get());
          }
          ticket.setAssignee(assignees);
          ticket = ticketRepository.save(ticket);
          TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);
          convertToUser(ticket, ticketResponseDto);
          log.info("TicketService : createTicket Execution End");
          return ticketResponseDto;
        }
      } else {
        log.error("TicketService : Project With The Id Not Found");
        throw new ProjectNotFoundException("Project With The Id Not Found");
      }
    } catch (Exception e) {
      throw e;
    }
  }

  public List<TicketResponseDto> getAllTickets() {
    log.info("TicketService : getAllTickets Execution Started");
    try {
      List<Ticket> ticketList = ticketRepository.findAll();
      log.info("list "+ticketList);
      List<TicketResponseDto> responseTickets =
          ticketList.stream()
              .map(
                  ticket -> {
                    try {
                      TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);
                      convertToUser(ticket, ticketResponseDto);
                      log.info("ticketResponseDto "+ticketResponseDto);
                      return ticketResponseDto;
                    } catch (Exception e) {
                      e.printStackTrace();
                      return null;
                    }
                  })
              .collect(Collectors.toList());
      log.info("TicketService : getTickets Execution Ended");
      return responseTickets;
    } catch (Exception e) {
      throw new TicketNotFoundException("Falied to Load tickets ");
    }
  }

  public TicketResponseDto getTicketById(Long id) throws TicketNotFoundException {
    log.info("TicketService : getTicketById Execution Started");
    log.info("id" + id);
    Optional<Ticket> existingTicket = ticketRepository.findById(id);
    if (existingTicket.isPresent()) {
      TicketResponseDto ticketResponseDto = new TicketResponseDto(existingTicket.get());
      convertToUser(existingTicket.get(), ticketResponseDto);
      log.info("TicketService : getTicketById Execution Ended");
      return ticketResponseDto;
    } else {
      log.error("TicketService : Ticket not found with id: {} " ,id);
      throw new TicketNotFoundException("Ticket not found with id: " + id);
    }
  }

  public TicketResponseDto updateTicket(RequestBodyTicket requestBodyTicket, Long ticketId)
      throws TicketNotFoundException {
    try {
      log.info("TicketService : updateTicket Execution Started");
      Ticket ticket = requestBodyTicket.convertToEntity(requestBodyTicket);
      Optional<Project> projectOptional = projectRepo.findById(requestBodyTicket.getProjectId());
      if (projectOptional.isPresent()) {
        log.info("TicketService : Project Found With the Id ");
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);

        if (existingTicket.isPresent()) {
          log.info("TicketService : Ticket Found With the Id ");
          List<User> assignees = new ArrayList<>();
          for (Long userId : requestBodyTicket.getAssignee()) {
            User user = userRepo.findById(userId).orElse(null);
            if(user != null)
              assignees.add(user);
          }
          existingTicket.get().setAssignee(assignees);
          clone(existingTicket.get(), ticket);
          ticket = ticketRepository.save(existingTicket.get());
          TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);
          convertToUser(existingTicket.get(), ticketResponseDto);
          log.info("TicketService : updateTicket Execution Ended");
          return ticketResponseDto;
        } else {
          log.error("TicketService : Ticket not found with id {} ",ticket.getId());
          throw new TicketNotFoundException("Ticket not found with id:  " + ticket.getId());
        }
      } else {
        log.error("TicketService : Project With the ID Not Found");
        throw new ProjectNotFoundException("Project With the ID Not Found");
      }
    } catch (Exception e) {
      throw e;
    }
  }

  public void clone(Ticket existing, Ticket updated) {
    existing.setTicketId(updated.getTicketId());
    existing.setName(updated.getName());
    existing.setStatus(updated.getStatus());
    existing.setPriority(updated.getPriority());
    existing.setEndDate(updated.getEndDate());
    existing.setCreatedBy(updated.getCreatedBy());
    existing.setDescription(updated.getDescription());
    existing.setAccountableAssignee(updated.getAccountableAssignee());
    existing.setCustomFields(updated.getCustomFields());
  }

  public void deleteTicket(Long id) {
    try {
      log.info("TicketService : deleteTicket Execution Started");
      Optional<Ticket> ticket = ticketRepository.findById(id);
      if (ticket.isPresent()) {
        log.info("TicketService : deleteTicket Execution Ended");
        ticketRepository.deleteById(id);
      } else {
        log.error("TicketService : Ticket Not Found");
        throw new TicketNotFoundException("Ticket Not Found");
      }

    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * this is to get all the tickets from user id
   *
   * @param
   * @return list of tickets
   */
  public List<TicketResponseDto> getTicketsByAssignee(Long userId) {
    try {
      log.info("TicketService : getTicketsByAssignee Execution Started");
      List<Ticket> ticketList = ticketRepository.findByAssignee_Id(userId);
      List<TicketResponseDto> responseTickets =
          ticketList.stream()
              .map(
                  ticket -> {
                    try {
                      TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);
                      convertToUser(ticket, ticketResponseDto);
                      return ticketResponseDto;
                    } catch (Exception e) {
                      e.printStackTrace();
                      return null;
                    }
                  })
                  .filter(Objects::nonNull)
                  .collect(Collectors.toList());
      log.info("TicketService : deleteTicket Execution Ended");
      return responseTickets;
    } catch (Exception e) {
      throw e;
    }
  }

  public List<TicketResponseDto> getTicketsByProject(Long projectId) {
    try{
      log.info("TicketService : getTicketsByProject Execution Started");
      List<Ticket> ticketList = ticketRepository.findByProjectId(projectId);
      List<TicketResponseDto> responseTickets =
              ticketList.stream()
                      .map(
                              ticket -> {
                                try {
                                  TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);
                                  convertToUser(ticket, ticketResponseDto);
                                  return ticketResponseDto;
                                } catch (Exception e) {
                                  e.printStackTrace();
                                  return null;
                                }
                              })
                      .collect(Collectors.toList());
      log.info("TicketService : getTicketsByProject Execution Ended");
      return responseTickets;
    }
    catch (Exception e){
      throw e;
    }

  }

  public void convertToUser(Ticket ticket, TicketResponseDto ticketResponseDto) {
    try {
      User createdBy = getUserById(ticket.getCreatedBy());
      User accountableAssignee = getUserById(ticket.getAccountableAssignee());

      ticketResponseDto.setCreatedBy(new UserDto(createdBy.getId(), createdBy.getUserName(), createdBy.getEmail()));
      ticketResponseDto.setAccountableAssigneeName(new UserDto(accountableAssignee.getId(), accountableAssignee.getUserName(), accountableAssignee.getEmail()));

      List<UserDto> assigneeUserDtos = ticket.getAssignee().stream()
              .map(user -> new UserDto(user.getId(), user.getUserName(), user.getEmail()))
              .collect(Collectors.toList());

      Project project = getProjectById(ticket.getProjectId());
      List<CustomField> customFieldList = getCustomFieldsByProjectId(ticket.getProjectId());

      ticketResponseDto.setProject(new ProjectDto(project.getId(), project.getName()));
      ticketResponseDto.setCustomFieldList(customFieldList);
      ticketResponseDto.setAssigneeName(assigneeUserDtos);
    } catch (Exception e) {
      log.error("Failed to convert ticket to user DTO: {}", e.getMessage());
      throw e;
    }
  }

  private User getUserById(Long userId) {
    return userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
  }

  private Project getProjectById(Long projectId) {
    return projectRepo.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
  }

  private List<CustomField> getCustomFieldsByProjectId(Long projectId) {
    return customizedRepository.findByProjectId(projectId);
  }

}
