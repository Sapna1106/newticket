package com.example.TicketModule.controller;

import com.example.TicketModule.Dto.ApiResponse;
import com.example.TicketModule.Dto.reestimated.ReestimatedTicketRequestDTO;
import com.example.TicketModule.Dto.reestimated.ReestimatedTicketResponseDTO;
import com.example.TicketModule.exception.ReestimatedTicketAlreadyPresent;
import com.example.TicketModule.exception.ReestimationCreationException;
import com.example.TicketModule.exception.ReestimationNotFoundException;
import com.example.TicketModule.service.ReestimatedTicketService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tickets")
public class ReestimatedTicketController {
    private final Logger log = LoggerFactory.getLogger(ReestimatedTicketController.class);

    @Autowired
    private ReestimatedTicketService reestimatedTicketService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/{id}/re-estimation")
    public ResponseEntity<ApiResponse> createReestimatedTicket(@PathVariable Long id, @RequestBody ReestimatedTicketRequestDTO reestimatedTicket) {
        log.info("Reestimated Ticket Controller : Inside create reestimation ticket");
        try {
            log.info("Reestimated Ticket Controller : Creating Reestimated Ticket");
            ReestimatedTicketResponseDTO createdReestimatedTicket = reestimatedTicketService.createReestimatedTicket(id, reestimatedTicket);
            return ResponseEntity.ok()
                    .body(
                            new ApiResponse<ReestimatedTicketResponseDTO>(
                                    "Reestimation created successfully", createdReestimatedTicket, "OK"));

        } catch (ReestimationCreationException | ReestimatedTicketAlreadyPresent e) {
            log.error("Reestimated Ticket Controller : "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null, "BAD_REQUEST"));
        } catch (Exception e) {
            log.error("Reestimated Ticket Controller : "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while creating a new ticket", null, "INTERNAL_SERVER_ERROR"));
        }
    }

    @GetMapping("/{id}/re-estimation")
    public ResponseEntity<List<ReestimatedTicketResponseDTO>> getReestimatedTicketByUserId(@PathVariable Long id) {
        log.info("Reestimated Ticket Controller : Inside getReestimatedTicketByUserId");
        try {
            log.info("Reestimated Ticket Controller : Fetching Reestimated Tickets by User ID");
            List<ReestimatedTicketResponseDTO> reestimatedTicketDTO = reestimatedTicketService.getReestimatedTicketByUserId(id);
            return new ResponseEntity<>(reestimatedTicketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Reestimated Ticket Controller : "+e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/reestimation-history")
    public  ResponseEntity<List<ReestimatedTicketResponseDTO>> getReestimatedAllTicketByUserId(@PathVariable Long id) {
        log.info("Reestimated Ticket Controller : Inside getReestimatedAllTicketByUserId ");
        try {
            log.info("Reestimated Ticket Controller : Fetching Reestimated Tickets by User ID");
            List<ReestimatedTicketResponseDTO> reestimatedTicketDTO = reestimatedTicketService.getReestimatedAllTicketByUserId(id);
            return new ResponseEntity<>(reestimatedTicketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Reestimated Ticket Controller : "+e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/re-estimation")
    public ResponseEntity<ApiResponse> updateReestimatedTicket(@PathVariable Long id, @RequestBody ReestimatedTicketRequestDTO reestimatedTicketRequestDTO) {
        log.info("Reestimated Ticket Controller : Inside updateReestimatedTicket");
        try {
            log.info("Reestimated Ticket Controller : Updating Reestimated Ticket");
            ReestimatedTicketResponseDTO updatedReestimatedTicket = reestimatedTicketService.updateReestimatedTicket(id, reestimatedTicketRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<ReestimatedTicketResponseDTO>("Reestimation of Ticket updated successfully", updatedReestimatedTicket, "CREATED"));
        }  catch (ReestimationNotFoundException e) {
            log.error("Reestimated Ticket Controller : "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
        } catch (Exception e) {
            log.error("Reestimated Ticket Controller : "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while updating reestimated ticket", null, "INTERNAL_SERVER_ERROR"));
        }
    }
}