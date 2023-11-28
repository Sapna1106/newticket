package com.example.TicketModule.Controller;

import com.example.TicketModule.DTO.ApiResponse;
import com.example.TicketModule.DTO.ResponseToSend;
import com.example.TicketModule.DTO.TicketResponseDto;
import com.example.TicketModule.Exception.TicketCreationException;
import com.example.TicketModule.Exception.TicketNotFoundException;
import com.example.TicketModule.Exception.UserNotFoundException;
import com.example.TicketModule.Repository.TicketRepository;
import com.example.TicketModule.Service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TicketModule.Entity.Ticket;
import com.example.TicketModule.DTO.RequestBodyTicket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tickets")
public class TicketController {
  private static final Logger log = LoggerFactory.getLogger(TicketController.class);
  @Autowired private TicketService ticketService;

  @PostMapping("/addTicket")
  public ResponseEntity<?> createTicket(@RequestBody RequestBodyTicket newTicket) {
    log.info("inside create ticket");
    //        Map<String, Object> response = new HashMap<>();
    //        HttpStatus status = null;
    try {
      log.info("inside ticket creation try block");
      TicketResponseDto createdTicket = ticketService.createTicket(newTicket);
      log.info("222");

      return ResponseEntity.ok()
          .body(
              new ApiResponse<TicketResponseDto>(
                  "Ticket created successfully", createdTicket, 200));
    } catch (TicketCreationException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(e.getMessage(), null, 405));
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>("An error occurred while creating a new ticket", null, 405));
    }
  }

  @GetMapping
  public ResponseEntity<?> getTickets() {
    log.info("inside get all tickets");
    try {
      List<Ticket> tickets = ticketService.getTickets();
      return ResponseEntity.ok()
              .body(
                      new ApiResponse<List<Ticket>>(
                              "Ticket created successfully", tickets, 200));
    } catch (TicketNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(new ApiResponse<>(e.getMessage(), null, 405));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ApiResponse<>("An error occurred while creating a new ticket", null, 405));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getTicketById(@PathVariable Long id) {
    log.info("inside get Tickets by id");
    Map<String, Object> response = new HashMap<>();
    HttpStatus status = null;

    try {
      Optional<Ticket> ticket = ticketService.getTicketById(id);
      TicketResponseDto ticketResponseDto = new TicketResponseDto();
      ticketResponseDto = ticketResponseDto.convertToDtos(ticket.get(), ticketResponseDto);
      ticketService.convertTouser(ticket.get(),ticketResponseDto);
      response.put("message", "Success");
      response.put("data", ticketResponseDto);
      status = HttpStatus.OK;
    } catch (TicketNotFoundException e) {
      response.put("message", e.getMessage());
      response.put("data", null);
      status = HttpStatus.NOT_FOUND;
    } catch (Exception e) {
      response.put("message", "An error occurred");
      response.put("data", null);
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return new ResponseEntity<>(response, status);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> updateTicket(
      @RequestBody RequestBodyTicket requestBodyTicket) {
    log.info("inside update ticket method");
    Map<String, Object> response = new HashMap<>();
    HttpStatus status = HttpStatus.CREATED;
    // System.out.println(updatedTickket);
    try {
      Ticket updatedTicket = ticketService.updateTicket(requestBodyTicket);
      TicketResponseDto ticketResponseDto = new TicketResponseDto();
      ticketResponseDto = ticketResponseDto.convertToDtos(updatedTicket, ticketResponseDto);
      if (updatedTicket != null) {
        response.put("message", "Ticket updated successfully");
        response.put("data", ticketResponseDto);
        status = HttpStatus.OK;
      } else {
        response.put("message", "Ticket not found with id: " + updatedTicket.getId());
        response.put("data", null);
        status = HttpStatus.NOT_FOUND;
      }
    } catch (TicketNotFoundException e) {
      response.put("message", e.getMessage());
      response.put("data", null);
      status = HttpStatus.NOT_FOUND;
    } catch (Exception e) {
      response.put("message", "An error occurred");
      response.put("data", null);
      log.info(String.valueOf(e));
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return new ResponseEntity<>(response, status);
  }

  //    @PutMapping("/{id}/otherLinks")
  //    public ResponseEntity<Map<String, Object>> updateTicketWithOtherLinks(@PathVariable String
  // id, @RequestBody OtherLinks otherLinks) {
  //        Map<String, Object> response = new HashMap<>();
  //        HttpStatus status = null;
  //
  //
  //        try {
  //            Ticket ticket= ticketService.updateTicketWithOtherLinks(id, otherLinks);
  //            System.out.println("this is ticket for frontend"+ticket);
  //            response.put("message", "Success");
  //            response.put("data", ticket);
  //            status = HttpStatus.OK;
  //        } catch (IllegalArgumentException e) {
  //            System.out.println("inside the exception");
  //            response.put("message", e.getMessage());
  //            response.put("data", null);
  //            status = HttpStatus.BAD_REQUEST;
  //        } catch (Exception e) {
  //            response.put("message", "An error occurred");
  //            response.put("data", null);
  //            status = HttpStatus.INTERNAL_SERVER_ERROR;
  //        }
  //
  //        return new ResponseEntity<>(response, status);
  //
  //
  //    }

  /**
   * delete a ticket by ticket id
   *
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  public ResponseToSend<?> deleteTicket(@PathVariable Long id) {
    log.info("inside delete ticket");
    try {
      boolean status = ticketService.deleteTicket(id);
      if (status) {
        return new ResponseToSend<>(
            "Ticket with ID " + id + " has been deleted.", null, HttpStatus.OK);
      } else {
        return new ResponseToSend<>(
            "Error occurred while deleting.", null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (Exception e) {
      return new ResponseToSend<>("An error occurred", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  //    @GetMapping("/byAssignee/{userId}")
  //    public ResponseEntity<Map<String, Object>> getTicketsByAssignee(@PathVariable String userId)
  // {
  //        Map<String, Object> response = new HashMap<>();
  //        HttpStatus status;
  //
  //        try {
  //            List<Ticket> tickets = ticketService.getTicketsByAssignee(userId);
  //            response.put("message", "Success");
  //            response.put("data", tickets);
  //            status = HttpStatus.OK;
  //        } catch (UserNotFoundException e) {
  //            response.put("message", e.getMessage());
  //            response.put("data", null);
  //            status = HttpStatus.NOT_FOUND;
  //        } catch (Exception e) {
  //            response.put("message", "An error occurred");
  //            response.put("data", null);
  //            status = HttpStatus.INTERNAL_SERVER_ERROR;
  //        }
  //
  //        return new ResponseEntity<>(response, status);
  //    }

      @GetMapping("/byProject/{projectId}")
      public ResponseToSend<List<TicketResponseDto>> getTicketsByProject(@PathVariable Long projectId) {
          log.info("get tickets list  by project id" );
          try {
              List<TicketResponseDto> tickets = ticketService.getTicketsByProject(projectId);
              return new ResponseToSend<>("Success", tickets, HttpStatus.OK);
          } catch (Exception e) {
              return new ResponseToSend<>("An error occurred", null,
   HttpStatus.INTERNAL_SERVER_ERROR);
          }
      }

  @PostMapping("/testPost")
  public ResponseEntity<String> testPost(@RequestBody RequestBodyTicket newTicket) {
    return ResponseEntity.status(HttpStatus.OK).body("Hii");
  }

  @GetMapping("/testGet")
  public ResponseEntity<String> testGet(@RequestBody RequestBodyTicket newTicket) {
    return ResponseEntity.status(HttpStatus.OK).body("Hii");
  }
}
