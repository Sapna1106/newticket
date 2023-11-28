package com.example.TicketModule.Service;

import com.example.TicketModule.Controller.TicketController;
import com.example.TicketModule.DTO.RequestBodyTicket;
import com.example.TicketModule.DTO.TicketResponseDto;
import com.example.TicketModule.DTO.UserDto;
import com.example.TicketModule.Entity.Project;
import com.example.TicketModule.Entity.Ticket;
import com.example.TicketModule.Entity.User;
import com.example.TicketModule.Exception.TicketCreationException;
import com.example.TicketModule.Exception.TicketNotFoundException;
import com.example.TicketModule.Exception.UserNotFoundException;
import com.example.TicketModule.Repository.ProjectRepository;
import com.example.TicketModule.Repository.TicketRepository;
import com.example.TicketModule.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

  private static final Logger log = LoggerFactory.getLogger(TicketController.class);
  @Autowired private TicketRepository ticketRepository;
  @Autowired private ProjectRepository projectRepo;
  @Autowired private UserRepository userRepo;

  @Autowired private ModelMapper modelMapper;

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
    log.info("ticket" + newTicket);
    Ticket ticket = newTicket.convertToEntity(newTicket);
    log.info("rrr");
    System.out.println(newTicket.getProjectId());
    Project project = projectRepo.findById(newTicket.getProjectId()).orElse(null);
    System.out.println(project);
    try {
      log.info("inside try ");
      if (project != null) {
        log.info("project is not null");
        List<Ticket> ticketsForProject = ticketRepository.findByProjectId(newTicket.getProjectId());

        if (!ticketsForProject.isEmpty()) {
          System.out.println("list is not empty");
          Ticket lastTicket = ticketsForProject.get(ticketsForProject.size() - 1);
          String lastCustomId = lastTicket.getTicketId();

          System.out.println(lastCustomId);
          String projectInitials = lastCustomId.split("-")[0];
          int lastTicketNumber = Integer.parseInt(lastCustomId.split("-")[1]);

          // Increment the number for the new ticket
          int newTicketNumber = lastTicketNumber + 1;
          ticket.setTicketId(projectInitials+"-"+newTicketNumber);
          // System.out.println(ticket.getCustomId());

          List<User> assignees  = new ArrayList<>();
          for(Long userId:newTicket.getAssignee()){
            assignees.add(userRepo.findById(userId).get());
          }
          ticket.setAssignee(assignees);
          ticket = ticketRepository.save(ticket);
          log.info("create servce Completed");
          TicketResponseDto ticketResponseDto = new TicketResponseDto();
        ticketResponseDto = ticketResponseDto.convertToDtos(ticket, ticketResponseDto);
        convertTouser(ticket,ticketResponseDto);
          return ticketResponseDto;


        }else {
          ticket.setTicketId(project.getInitials().toUpperCase()+"-1");
          // System.out.println("list is  empty");
          // System.out.println("want to genrated coustomised id: "+project.getInitials().toUpperCase()+"-1");

          List<User> assignees  = new ArrayList<>();
          for(Long userId:newTicket.getAssignee()){
            assignees.add(userRepo.findById(userId).get());
          }
          ticket.setAssignee(assignees);
          ticket = ticketRepository.save(ticket);
          log.info("create servce Completed");
          TicketResponseDto ticketResponseDto = new TicketResponseDto();
          ticketResponseDto = ticketResponseDto.convertToDtos(ticket, ticketResponseDto);
          convertTouser(ticket,ticketResponseDto);
          return ticketResponseDto;

        }
      } else {
        log.info("else  " + project.getInitials() + "-1");
        return null;
      }
    } catch (Exception e) {
      throw new TicketCreationException("Failed to create ticket. Please try again later.");
    }
  }

  public List<Ticket> getTickets() {
    try {
      return ticketRepository.findAll();
    } catch (Exception e) {
      throw new TicketNotFoundException("Falied to Load tickets ");
    }
  }

  public Optional<Ticket> getTicketById(Long id) throws TicketNotFoundException {
    log.info("id" + id);
    Optional<Ticket> existingTicket = ticketRepository.findById(id);

    if (existingTicket.isPresent()) {
      return existingTicket;
    } else {
      throw new TicketNotFoundException("Ticket not found with id: " + id);
    }
  }

  public Ticket updateTicket(RequestBodyTicket requestBodyTicket) throws TicketNotFoundException {
    log.info("update Ticket Exicutation Started");
    Ticket ticket = requestBodyTicket.convertToEntity(requestBodyTicket);
    Optional<Project> project = projectRepo.findById(requestBodyTicket.getProjectId());
    System.out.println("project is :" + project.get());
    //        ticket.setProjectId(project.get());
    Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
    if (existingTicket.isPresent()) {
      User createdBy = userRepo.findById(requestBodyTicket.getCreatedBy()).get();
      List<User> assignees = new ArrayList<>();
      for (Long userId : requestBodyTicket.getAssignee()) {
        assignees.add(userRepo.findById(userId).get());
      }

      ticket = ticketRepository.save(ticket);

      System.out.println("ticket is " + ticket);
      return ticket;
    } else {
      throw new TicketNotFoundException("Ticket not found with id:  " + ticket.getId());
    }
  }

  public Boolean deleteTicket(Long id) {
    try {
      ticketRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * this is to get all the tickets from user id
   *
   * @param
   * @return list of tickets
   */
  //    public List<Ticket> getTicketsByAssignee( String userId) {
  //        try {
  //            System.out.println("hi this is find by user id");
  //            return ticketRepository.findByUsersAssignedTo_Id(userId);
  //        } catch (Exception e) {
  //            throw new UserNotFoundException("User not found with ID: " + userId);
  //        }
  //    }

      public List<TicketResponseDto> getTicketsByProject( Long projectId) {
          List<Ticket> ticketList = ticketRepository.findByProjectId(projectId);
    List<TicketResponseDto> responseTickets =
        ticketList.stream()
            .map(
                ticket -> {
                  try {
                    TicketResponseDto ticketResponseDto = new TicketResponseDto();
                    ticketResponseDto = ticketResponseDto.convertToDtos(ticket,ticketResponseDto);
                    convertTouser(ticket,ticketResponseDto);
                    return ticketResponseDto;
                  } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                  }
                })
            .collect(Collectors.toList());
        return responseTickets;
      }

  //    public List<Ticket> getTicketsByParent( String parentId) {
  //        return ticketRepository.findByParent_Id(parentId);
  //    }
  //
  //        public void deleteTicketsByProjectId(String projectId) {
  //            ticketRepository.deleteByProjectIn_Id(projectId);
  //        }

  public void convertTouser(Ticket ticket, TicketResponseDto ticketResponseDto) {
    User createdBy = userRepo.findById(ticket.getCreatedBy()).get();
    User assocutableAsigne = userRepo.findById(ticket.getAccountableAssignee()).get();
    ticketResponseDto.setCreatedBy(
        new UserDto(createdBy.getId(), createdBy.getUserName(), createdBy.getEmail()));
    ticketResponseDto.setAccountableAssigneeName(
        new UserDto(
            assocutableAsigne.getId(),
            assocutableAsigne.getUserName(),
            assocutableAsigne.getEmail()));
    List<UserDto> userDtos = new ArrayList<>();
    for (User user : ticket.getAssignee()) {
      userDtos.add(new UserDto(user.getId(), user.getUserName(), user.getEmail()));
    }
    ticketResponseDto.setAssigneeName(userDtos);
    return;
  }
}