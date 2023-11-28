package com.example.TicketModule.Service;

import com.example.TicketModule.DTO.ReestimatedTicketDTO;
import com.example.TicketModule.Entity.ReestimatedTicket;
import com.example.TicketModule.Entity.Ticket;
import com.example.TicketModule.Entity.User;
import com.example.TicketModule.Exception.ReestimationCreationException;
import com.example.TicketModule.Exception.TicketNotFoundException;
import com.example.TicketModule.Repository.ReestimatedRepository;
import com.example.TicketModule.Repository.TicketRepository;
import com.example.TicketModule.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReestimatedTicketService {

    private final Logger logger = LoggerFactory.getLogger(ReestimatedTicketService.class);

    @Autowired
    private ReestimatedRepository reestimatedTicketRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public ReestimatedTicket createReestimatedTicket(Long id, ReestimatedTicket reestimatedTicket) {
        try {
            Optional<Ticket> currentTicket = ticketRepository.findById(id);

            if (currentTicket.isPresent()) {
                reestimatedTicket.setTicketId(currentTicket.get());
                User assignedTo = currentTicket.get().getCreatedBy();
                reestimatedTicket.setAssignedTo(assignedTo);

                Long reestimatedId = reestimatedTicket.getReestimatedBy().getId();
                Optional<User> reestimatedBy = userRepository.findById(reestimatedId);
                reestimatedTicket.setReestimatedBy(reestimatedBy.get());

                return reestimatedTicketRepository.save(reestimatedTicket);
            } else {
                throw new TicketNotFoundException("Ticket not found");
            }
        } catch (Exception e) {
            logger.error("Failed to create reestimated ticket", e);
            throw new ReestimationCreationException("Failed to create reestimated ticket. Please try again later.");
        }
    }

    public List<ReestimatedTicketDTO> getReestimatedTicketByUserId(Long id) {
        try {
            List<ReestimatedTicket> reestimatedTickets = reestimatedTicketRepository.findByStatusAndAssignedToId("Pending", id);
            List<ReestimatedTicketDTO> reestimatedTicketDTO = reestimatedTickets.stream()
                    .map(ticket -> {
                        try {
                            return new ReestimatedTicketDTO(ticket);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Handle the exception as needed
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
            return reestimatedTicketDTO;
        } catch (Exception e) {
            logger.error("An error occurred while fetching reestimated tickets by user ID", e);
            throw e; // You might want to handle this more specifically based on your requirements
        }
    }

//    public Optional<List<ReestimatedTicket>> getReestimatedAllTicketByUserId(Long id) {
//        try {
//            return reestimatedTicketRepository.findByAssignedTo(id);
//        } catch (Exception e) {
//            logger.error("An error occurred while fetching all reestimated tickets by assignee/user ID", e);
//            throw e; // You might want to handle this more specifically based on your requirements
//        }
//    }
//
//    public ReestimatedTicket updateReestimatedTicket(Long id, ReestimatedTicket reestimatedTicket) {
//        try {
//            Optional<ReestimatedTicket> existingReestimatedTicket = reestimatedTicketRepository.findById(id);
//
//            if (existingReestimatedTicket.isPresent()) {
//                ReestimatedTicket updatedTicket = existingReestimatedTicket.get();
//
//                if ("Approved".equals(reestimatedTicket.getStatus())) {
//                    updatedTicket.setStatus(reestimatedTicket.getStatus());
//
//                    Long ticketId = updatedTicket.getTicketId().getId();
//
//                    Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
//                    if (ticket != null) {
//                        ticket.setEndDate(updatedTicket.getNewDate());
//                        ticketRepository.save(ticket);
//                    }
//                } else {
//                    updatedTicket.setStatus(reestimatedTicket.getStatus());
//                    updatedTicket.setDenyReason(reestimatedTicket.getDenyReason());
//                }
//
//                return reestimatedTicketRepository.save(updatedTicket);
//
//            } else {
//                throw new ReestimationNotFoundException("Reestimated Ticket not found with ID: " + id);
//            }
//        } catch (Exception e) {
//            logger.error("An error occurred while updating reestimated ticket", e);
//            throw e; // You might want to handle this more specifically based on your requirements
//        }
//    }
}
