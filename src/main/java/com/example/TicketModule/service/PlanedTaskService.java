package com.example.TicketModule.service;

import com.example.TicketModule.Dto.PlanedTaskDto;
import com.example.TicketModule.Dto.tickets.TicketResponseDto;
import com.example.TicketModule.entity.PlanedTask;
import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.exception.TicketNotFoundException;
import com.example.TicketModule.repository.PlanedTaskRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlanedTaskService {
    @Autowired
    private PlanedTaskRepository planedTaskRepository;

    @Autowired
    private TicketService ticketService;

    public PlanedTaskDto createPlanedTask(PlanedTask userTicket) {
        try {
            log.info("PlanedTaskService: createPlanedTask Execution Started");

            // Save the PlanedTask
            PlanedTask planedTask = planedTaskRepository.save(userTicket);

            // Retrieve the associated Ticket
            Ticket ticket = ticketService.getTicketByIdTicket(planedTask.getTicket().getId());

            if (ticket != null) {
                log.info("PlanedTaskService: createPlanedTask - Ticket Found with ticket Id {}", ticket.getId());

                // Map PlanedTask and Ticket to DTOs
                PlanedTaskDto planedTaskDto = new PlanedTaskDto(planedTask);
                TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);

                // Convert Ticket to User and set it in PlanedTaskDto
                ticketService.convertToUser(ticket, ticketResponseDto);
                planedTaskDto.setTicket(ticketResponseDto);

                log.info("PlanedTaskService: createPlanedTask Execution Ended");
                return planedTaskDto;
            } else {
                throw new TicketNotFoundException("Ticket With the Id Does Not Exist");
            }
        } catch (Exception e) {
            log.error("PlanedTaskService: createPlanedTask error occurred", e);
            throw e; // You may want to handle or throw a specific exception based on your application needs
        }
    }

    public PlanedTask updatePlanedTask(Long userId, Long ticketId, PlanedTask newUserTicketData) {
        try {
            log.info("PlanedTaskService: updatePlanedTask Execution Started");

            PlanedTask existingUserTicket = planedTaskRepository.findByUserIdAndTicketId(userId, ticketId);

            if (existingUserTicket == null) {
                throw new TicketNotFoundException("Ticket not found for userId: " + userId + " and ticketId: " + ticketId);
            }

            // Update the fields with the new data
            existingUserTicket.setDropDate(newUserTicketData.getDropDate());
            existingUserTicket.setStartTicketTime(newUserTicketData.getStartTicketTime());
            existingUserTicket.setEndTicketTime(newUserTicketData.getEndTicketTime());

            // Save the updated user ticket
            PlanedTask updatedUserTicket = planedTaskRepository.save(existingUserTicket);

            log.info("PlanedTaskService: updatePlanedTask Execution Ended");
            return updatedUserTicket;
        } catch (Exception e) {
            log.error("PlanedTaskService: updatePlanedTask error occurred", e);
            throw e; // You may want to handle or throw a specific exception based on your application needs
        }
    }

    @Transactional
    public void deletePlanedTask(Long userId, Long ticketId) {
        try {
            log.info("PlanedTaskService: deletePlanedTask Execution Started");
//            PlanedTask optionalPlanedTask = planedTaskRepository.findFromUserTicketByUserIdAndTicketId(userId,ticketId);
//            if(optionalPlanedTask != null){
                planedTaskRepository.deleteFromUserTicketByUserIdAndTicketId(userId, ticketId);

                log.info("PlanedTaskService: deletePlanedTask Execution Ended");
//            }else {
//                throw new PlanedTaskNotFoundException("Plane found");
//            }
        } catch (Exception e) {
            log.error("PlanedTaskService: deletePlanedTask error occurred", e);
            throw e; // You may want to handle or throw a specific exception based on your application needs
        }
    }

    public List<PlanedTaskDto> getAllPlanedTask(Long userId) {
        try {
            log.info("PlanedTaskService: getAllPlanedTask Execution Started");

            List<PlanedTask> userTickets = planedTaskRepository.findByUserId(userId);
            List<PlanedTaskDto> planedTaskDtos = userTickets.stream()
                    .map(
                            planedTask -> {
                                PlanedTaskDto planedTaskDto = new PlanedTaskDto(planedTask);
                                TicketResponseDto ticketResponseDto = new TicketResponseDto(planedTask.getTicket());
                                ticketService.convertToUser(planedTask.getTicket(), ticketResponseDto);
                                planedTaskDto.setTicket(ticketResponseDto);
                                return planedTaskDto;
                            })
                    .collect(Collectors.toList());

            log.info("PlanedTaskService: getAllPlanedTask Execution Ended");
            return planedTaskDtos;
        } catch (Exception e) {
            log.error("PlanedTaskService: getAllPlanedTask error occurred", e);
            throw e; // You may want to handle or throw a specific exception based on your application needs
        }
    }
}
