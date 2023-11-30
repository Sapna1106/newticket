package com.example.TicketModule.service;

import com.example.TicketModule.Dto.reestimated.ReestimatedTicketRequestDTO;
import com.example.TicketModule.Dto.reestimated.ReestimatedTicketResponseDTO;
import com.example.TicketModule.Dto.tickets.UserDto;
import com.example.TicketModule.entity.ReestimatedTicket;
import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.entity.User;
import com.example.TicketModule.exception.ReestimatedTicketAlreadyPresent;
import com.example.TicketModule.exception.ReestimationCreationException;
import com.example.TicketModule.exception.ReestimationNotFoundException;
import com.example.TicketModule.exception.TicketNotFoundException;
import com.example.TicketModule.repository.ReestimatedRepository;
import com.example.TicketModule.repository.TicketRepository;
import com.example.TicketModule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReestimatedTicketService {

    private final Logger log = LoggerFactory.getLogger(ReestimatedTicketService.class);

    @Autowired private ReestimatedRepository reestimatedTicketRepository;

    @Autowired private TicketRepository ticketRepository;

    @Autowired private UserRepository userRepository;

    public ReestimatedTicketResponseDTO createReestimatedTicket(Long id, ReestimatedTicketRequestDTO reestimatedTicket) {
        log.info("Reestimated Ticket Service : create reestimation execution started");
        ReestimatedTicket newReestimatedTicket= reestimatedTicket.convertToEntity(reestimatedTicket);
        Optional<Ticket> currentTicket = ticketRepository.findById(id);
        ReestimatedTicket ticketId= reestimatedTicketRepository.findByTicketId(currentTicket.get());
        if(ticketId == null){
            try {
                log.info("Reestimated Ticket Service : inside try");
                if (currentTicket.isPresent()) {
                    log.info("Reestimated Ticket Service : inside if");
                    newReestimatedTicket.setTicket(currentTicket.get());
                    Long assignedTo = currentTicket.get().getCreatedBy();
                    newReestimatedTicket.setAssignedTo(assignedTo);

                    Long reestimatedId = reestimatedTicket.getReestimatedBy();
                    newReestimatedTicket.setReestimatedBy(reestimatedId);
                    newReestimatedTicket= reestimatedTicketRepository.save(newReestimatedTicket);
                    ReestimatedTicketResponseDTO reestimatedTicketResponseDTO= new ReestimatedTicketResponseDTO(newReestimatedTicket);
                    convertToUser(newReestimatedTicket, reestimatedTicketResponseDTO);
                    return reestimatedTicketResponseDTO;
                } else {
                    log.error("Reestimated Ticket Service : Ticket not found");
                    throw new TicketNotFoundException("Ticket not found");
                }
            } catch (Exception e) {
                log.error("Reestimated Ticket Service : Failed to create reestimated ticket", e);
                throw new ReestimationCreationException("Failed to create reestimated ticket. Please try again later.");
            }
        }else{
            log.error("Reestimated Ticket Service : Reestimation for this ticket is already present");
            throw new ReestimatedTicketAlreadyPresent("Reestimation for this ticket is already present");
        }

    }

    public List<ReestimatedTicketResponseDTO> getReestimatedTicketByUserId(Long id) {
        log.info("Reestimated Ticket Service : inside getReestimatedTicketByUserId");
        try {
            log.info("Reestimated Ticket Service : inside try");
            List<ReestimatedTicket> reestimatedTickets = reestimatedTicketRepository.findByStatusAndAssignedTo("Pending", id);
            List<ReestimatedTicketResponseDTO> reestimatedTicketDTO =
                    reestimatedTickets.stream()
                            .map(
                                    ticket -> {
                                        try {
                                            log.info("Reestimated Ticket Service : inside try of map");
                                            ReestimatedTicketResponseDTO reestimatedTicketResponseDTO = new ReestimatedTicketResponseDTO(ticket);
                                            convertToUser(ticket, reestimatedTicketResponseDTO);
                                            return reestimatedTicketResponseDTO;
                                        } catch (Exception e) {
                                            log.error("Reestimated Ticket Service : Error fetching data");
                                            throw e;
                                        }
                                    })
                            .collect(Collectors.toList());
            return reestimatedTicketDTO;
        } catch (Exception e) {
            log.error("Reestimated Ticket Service : An error occurred while fetching all pending reestimated tickets by user ID", e);
            throw e;
        }
    }

    public List<ReestimatedTicketResponseDTO> getReestimatedAllTicketByUserId(Long id) {
        log.info("Reestimated Ticket Service : inside getReestimated all ticket by assignee id");
        try {
            log.info("Reestimated Ticket Service : inside try");
            List<ReestimatedTicket> reestimatedTickets = reestimatedTicketRepository.findByAssignedTo(id);
            List<ReestimatedTicketResponseDTO> reestimatedTicketDTO =
                    reestimatedTickets.stream()
                            .map(
                                    ticket -> {
                                        try {
                                            log.info("Reestimated Ticket Service : inside try of map");
                                            ReestimatedTicketResponseDTO reestimatedTicketResponseDTO = new ReestimatedTicketResponseDTO(ticket);
                                            convertToUser(ticket, reestimatedTicketResponseDTO);
                                            return reestimatedTicketResponseDTO;
                                        } catch (Exception e) {
                                            log.error("Reestimated Ticket Service : Error fetching data");
                                            throw e;
                                        }
                                    })
                            .collect(Collectors.toList());
            return reestimatedTicketDTO;
        } catch (Exception e) {
            log.error("Reestimated Ticket Service : An error occurred while fetching all reestimated tickets by user ID", e);
            throw e; // You might want to handle this more specifically based on your requirements
        }
    }

    public ReestimatedTicketResponseDTO updateReestimatedTicket(Long id, ReestimatedTicketRequestDTO reestimatedTicketRequestDTO) throws ReestimationNotFoundException {
        log.info("Reestimated Ticket Service : inside updateReestimatedTicket");
        try {
            log.info("Reestimated Ticket Service : Inside try");
            ReestimatedTicket reestimatedTicket=reestimatedTicketRequestDTO.convertToEntity(reestimatedTicketRequestDTO);
            Optional<ReestimatedTicket> existingReestimatedTicket = reestimatedTicketRepository.findById(id);
            if (existingReestimatedTicket.isPresent()) {
                log.info("Reestimated Ticket Service : Inside 1st if");
                ReestimatedTicket updatedTicket = existingReestimatedTicket.get();

                if ("Approved".equals(reestimatedTicketRequestDTO.getStatus())) {
                    log.info("Reestimated Ticket Service : inside 2nd if");
                    updatedTicket.setStatus(reestimatedTicket.getStatus());
                    Long ticketId = updatedTicket.getTicket().getId();
                    Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
                    if (ticket != null) {
                        log.info("Reestimated Ticket Service : inside 3rd if");
                        ticket.setEndDate(updatedTicket.getNewDate());
                        ticketRepository.save(ticket);
                    }
                } else {
                    log.info("Reestimated Ticket Service : inside denied logic");
                    updatedTicket.setStatus(reestimatedTicket.getStatus());
                    updatedTicket.setDenyReason(reestimatedTicket.getDenyReason());
                }
                updatedTicket= reestimatedTicketRepository.save(updatedTicket);
                ReestimatedTicketResponseDTO reestimatedTicketResponseDTO= new ReestimatedTicketResponseDTO(updatedTicket);
                convertToUser(existingReestimatedTicket.get(), reestimatedTicketResponseDTO);
                return reestimatedTicketResponseDTO;
            } else {
                log.error("Reestimated Ticket Service : Reestimated Ticket not found with ID: " + id);
                throw new ReestimationNotFoundException("Reestimated Ticket not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Reestimated Ticket Service : An error occurred while updating reestimated ticket", e);
            throw e;
        }
    }

    public void convertToUser(ReestimatedTicket ticket, ReestimatedTicketResponseDTO ticketResponseDto) {
        User assignedTo = userRepository.findById(ticket.getAssignedTo()).get();
        User reestimatedBy = userRepository.findById(ticket.getReestimatedBy()).get();
        ticketResponseDto.setAssignedToName(new UserDto( assignedTo.getId(), assignedTo.getUserName(), assignedTo.getEmail()));
        ticketResponseDto.setReestimatedByName(new UserDto( reestimatedBy.getId(), reestimatedBy.getUserName(), reestimatedBy.getEmail()));

    }

}