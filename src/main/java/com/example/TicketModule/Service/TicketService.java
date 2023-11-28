package com.example.TicketModule.Service;

import com.example.TicketModule.Controller.TicketController;
import com.example.TicketModule.DTO.RequestBodyTicket;
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

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    @Autowired
    private TicketRepository ticketRepository;

    //    @Autowired
    //    private OtherLinksRepo otherLinksRepo;
    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;


    public RequestBodyTicket createTicket(RequestBodyTicket newTicket) {
        log.info("ticket" + newTicket);

//        User user = userRepo.findById(newTicket.getCreatedBy()).orElseThrow();
        Ticket ticket = modelMapper.map(newTicket, Ticket.class);

//        ticket.setCreatedBy(user);

        //        Long projectId= newTicket.getProjectId();
        Project project = projectRepo.findById(newTicket.getProjectId()).orElse(null);
        ticket.setProjectId(project);

        try {
            log.info("inside try ");
            if (project != null) {
                log.info("project is not null");
                int ticketCountForProject = ticketRepository.countTicketsByProjectId(newTicket.getProjectId());
                log.info("ticketCountForProject" + ticketCountForProject);
//                    List<Ticket> ticketsForProject = ticketRepository.findByProjectId_Id(newTicket.getProjectId());

//                    if (ticketCountForProject != 0) {
//                        log.info("list is not empty");
//                        Ticket lastTicket = ticketsForProject.get(ticketsForProject.size() - 1);
//                        String lastCustomId = lastTicket.getTicketId();

//                        log.info(lastCustomId);
//                        String projectInitials = lastCustomId.split("-")[0];
//                        int lastTicketNumber = Integer.parseInt(lastCustomId.split("-")[1]);

                // Increment the number for the new ticket
                int newTicketNumber = ticketCountForProject + 1;
                ticket.setTicketId(project.getInitials().toUpperCase() + "-" + newTicketNumber);
                // System.out.println(ticket.getCustomId());

                Ticket ticket1 = ticketRepository.save(ticket);
                User createdBy = ticket1.getCreatedBy();

                log.info("created by is " + createdBy);

                log.info("ticket" + ticket1);

//                if (createdBy != null && createdBy.getId() != null) {
//                    log.info("if");
//                    createdBy = userRepo.findById(createdBy.getId()).orElse(null);
//                    log.info("else"+createdBy);
//                    ticket1.setCreatedBy(createdBy);
//                    log.info("ticket is "+ticket1);
//                }

//                RequestBodyTicket createdTicket = new RequestBodyTicket(ticket1.getId(), ticket1.getTicketId(), ticket1.getName() , ticket1.getDescription() , ticket1.getStartDate() , ticket1.getEndDate() , ticket1.getEndTime() , ticket1.getCreatedAt() , ticket1.getProjectId() , ticket1.getStatus() , ticket1.getPriority() , ticket1.getAssignee(), ticket1.getAccountableAssignee() , ticket1.getCustomFields() )
                RequestBodyTicket createdTicket=modelMapper.map(ticket1, RequestBodyTicket.class);
                return createdTicket;

//                    }else {
//                        ticket.setTicketId(project.getInitials().toUpperCase()+"-1");
//                        // System.out.println("list is  empty");
//                        // System.out.println("want to genrated coustomised id: "+project.getInitials().toUpperCase()+"-1");
//
//                        Ticket ticket1= ticketRepository.save(ticket);
//                        User createdBy = ticket1.getCreatedBy();
//                        log.info("created by is "+createdBy);
//                        if (createdBy != null && createdBy.getId() != null) {
//                            createdBy = userRepo.findById(createdBy.getId()).orElse(null);
//                            ticket1.setCreatedBy(createdBy);
//                            log.info("ticket is "+ticket1);
//                        }
//                        return  ticket1;
//
//                    }

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


    public Ticket updateTicket(Ticket ticket) throws TicketNotFoundException {
        log.info("id" + ticket.getProjectId().getId());

        Optional<Project> project = projectRepo.findById(ticket.getProjectId().getId());
        System.out.println("project is :" + project.get());
        ticket.setProjectId(project.get());
        System.out.println(ticket);
        Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
        if (existingTicket.isPresent()) {
            Ticket ticket1 = ticketRepository.save(ticket);
            User createdBy = ticket1.getCreatedBy();

            if (createdBy != null && createdBy.getId() != null) {
                createdBy = userRepo.findById(createdBy.getId()).orElse(null);
                ticket1.setCreatedBy(createdBy);
                System.out.println("ticket is " + ticket1);
            }
            return ticket1;
        } else {
            throw new TicketNotFoundException("Ticket not found with id: " + ticket.getId());
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
     * @param userId
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


    //    public List<Ticket> getTicketsByProject( Long projectId) {
    //        return ticketRepository.findByProjectId_Id(projectId);
    //    }

    //    public List<Ticket> getTicketsByParent( String parentId) {
    //        return ticketRepository.findByParent_Id(parentId);
    //    }
    //
//        public void deleteTicketsByProjectId(String projectId) {
//            ticketRepository.deleteByProjectIn_Id(projectId);
//        }

}
