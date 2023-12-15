package com.example.TicketModule.controller;

import com.example.TicketModule.dto.ApiResponse;
import com.example.TicketModule.dto.tickets.TicketResponseDto;
import com.example.TicketModule.exception.ProjectNotFoundException;
import com.example.TicketModule.exception.TicketCreationException;
import com.example.TicketModule.exception.TicketNotFoundException;
import com.example.TicketModule.exception.UserNotFoundException;
import com.example.TicketModule.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TicketModule.dto.tickets.RequestBodyTicket;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tickets")
public class TicketController {
  private static final Logger log = LoggerFactory.getLogger(TicketController.class);
  @Autowired private TicketService ticketService;

  @PostMapping("/addTicket")
  public ResponseEntity<ApiResponse> createTicket(@RequestBody RequestBodyTicket newTicket) {
    log.info("inside create ticket");
    try {
      log.info("inside ticket creation try block");
      TicketResponseDto createdTicket = ticketService.createTicket(newTicket);
      log.info("222");

      return ResponseEntity.ok()
          .body(
              new ApiResponse<TicketResponseDto>(
                  "Ticket created successfully", createdTicket, "OK"));
    } catch (TicketCreationException | UserNotFoundException | ProjectNotFoundException e ) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(e.getMessage(), null, "BAD_REQUEST"));
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("An error occurred while creating a new ticket", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getTickets() {
    log.info("inside get all tickets");
    try {
      List<TicketResponseDto> tickets = ticketService.getTickets();
      return ResponseEntity.ok()
          .body(
              new ApiResponse<List<TicketResponseDto>>(
                  "List of tickets Get successfully", tickets, "Ok"));
    } catch (TicketNotFoundException  e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(e.getMessage(), null, "BAD_REQUEST"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("An error occurred while getting tickets ticket", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getTicketById(@PathVariable Long id) {
    log.info("inside get Tickets by id");
    try {
      TicketResponseDto ticket = ticketService.getTicketById(id);
      return ResponseEntity.ok()
          .body(new ApiResponse<TicketResponseDto>("Ticket Get successfully", ticket, "OK"));
    } catch (TicketNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(e.getMessage(), null, "BAD_REQUEST"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("An error occurred while getting tickets", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updateTicket(
      @RequestBody RequestBodyTicket requestBodyTicket, @PathVariable Long id) {
    log.info("inside update ticket method");
    try {
      TicketResponseDto updatedTicket = ticketService.updateTicket(requestBodyTicket, id);
          return ResponseEntity.status(HttpStatus.CREATED)
                  .body(new ApiResponse<TicketResponseDto>("Ticket updated successfully", updatedTicket, "CREATED"));
    } catch (TicketNotFoundException | UserNotFoundException | ProjectNotFoundException e) {
      log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(e.getMessage(), null, "BAD_REQUEST"));
    } catch (Exception e) {
      log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("An error occurred while updating ticket", null, "INTERNAL_SERVER_ERROR"));
    }
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
  public ResponseEntity<ApiResponse> deleteTicket(@PathVariable Long id) {
    log.info("inside delete ticket");
    try {
      ticketService.deleteTicket(id);
      return ResponseEntity.ok()
              .body(new ApiResponse<>("Ticket updated successfully", false, "OK"));

    }
    catch(TicketNotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ApiResponse<>(e.getMessage(), false, "NOT_FOUND"));
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ApiResponse<>("Error occure while deleting ticket", false, "INTERNAL_SERVER_ERROR"));
    }
  }

  @GetMapping("/byAssignee/{userId}")
  public ResponseEntity<ApiResponse> getTicketsByAssignee(@PathVariable Long userId) {
    try {
      List<TicketResponseDto> tickets = ticketService.getTicketsByAssignee(userId);
      return ResponseEntity.ok()
              .body(new ApiResponse<List<TicketResponseDto>>("Ticket get By Assignee successfully", tickets, "Ok"));
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ApiResponse<>("Error occure while getting tickets by assigne Id", false, "INTERNAL_SERVER_ERROR"));
    }
  }

  @GetMapping("/byProject/{projectId}")
  public ResponseEntity<ApiResponse> getTicketsByProject(@PathVariable Long projectId) {
    log.info("get tickets list  by project id");
    try {
      List<TicketResponseDto> tickets = ticketService.getTicketsByProject(projectId);
      return ResponseEntity.ok().body(new ApiResponse<List<TicketResponseDto>>("",tickets,"OK"));
    }catch (TicketNotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ApiResponse<>("Error occure while getting tickets by assigne Id", null, "INTERNAL_SERVER_ERROR"));
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
