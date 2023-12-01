package com.example.TicketModule.controller;

import java.util.List;

import com.example.TicketModule.dto.ApiResponse;
import com.example.TicketModule.dto.WorklogsDTO;
import com.example.TicketModule.entity.Worklogs;
import com.example.TicketModule.exception.UserNotFoundException;
import com.example.TicketModule.exception.WorklogNotFoundException;
import com.example.TicketModule.service.TicketService;
import com.example.TicketModule.service.WorklogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/worklogs")
@Slf4j
public class WorklogController {
  @Autowired private WorklogsService worklogsService;

  @Autowired TicketService ticketService;

  /**
   * Get a list of all worklogs.
   *
   * @return A list of worklogs.
   */
  @GetMapping
  public ResponseEntity<ApiResponse> getAllWorklogs() {
    try {
      log.info("WorklogController : getAllWorklogs Execution Started");
      List<WorklogsDTO> worklogsDTOs = worklogsService.getAllWorklogs();
      log.info("WorklogController : getAllWorklogs Execution Ended");
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new ApiResponse<List<WorklogsDTO>>("List Of All The WorkLoges", worklogsDTOs, "OK"));
    } catch (UserNotFoundException e) {
      log.error(
          "WorklogController: An error occurred while creating a worklog: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception e) {
      log.error("Failed to retrieve all worklogs.", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<List<WorklogsDTO>>(
                  "Error Occur while Fetching all the Workflow", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  @GetMapping("/by-ticket/{ticketId}")
  public ResponseEntity<ApiResponse> getWorklogsByTicketIds(@PathVariable Long ticketId) {
    try {
      log.info("WorklogController: getWorklogsByTicketIds Execution Started");

      if (ticketId == null || ticketId <= 0) {
        log.error("WorklogController: Invalid ticket ID provided");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      List<WorklogsDTO> worklogsDTOs = worklogsService.getWorklogsByTicketId(ticketId);

      log.info(
          "WorklogController: Retrieved worklogs for ticket ID " + ticketId + " successfully.");
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new ApiResponse<List<WorklogsDTO>>("List Of All The WorkLoges", worklogsDTOs, "OK"));
    } catch (UserNotFoundException e) {
      log.error(
          "WorklogController: An error occurred while creating a worklog: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception ex) {
      log.error("WorklogController: Error occurred while fetching worklogs by ticket ID", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<List<WorklogsDTO>>(
                  "Error Occurred while Fetching all the Workflow", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  @GetMapping("/{ticketId}")
  public ResponseEntity<ApiResponse> getWorklogsByTicketId(@PathVariable Long ticketId) {
    try {
      log.info("WorklogController: getWorklogsByTicketId Execution Started");

      List<WorklogsDTO> worklogsDTOS = worklogsService.getWorklogsByTicketId(ticketId);

      log.info(
          "WorklogController: Retrieved worklogs for ticket ID " + ticketId + " successfully.");
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new ApiResponse<List<WorklogsDTO>>(
                  "List Of All The WorkLoges With TicketId", worklogsDTOS, "OK"));
    } catch (UserNotFoundException e) {
      log.error(
          "WorklogController: An error occurred while creating a worklog: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception e) {
      log.error("WorklogController: Failed to retrieve worklogs for ticket ID " + ticketId, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<List<WorklogsDTO>>(
                  "Error Occurred while Fetching all the Workflow", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  /**
   * Get a worklog by ID.
   *
   * @param logId The ID of the worklog to retrieve.
   * @return The worklog with the specified ID.
   */
  @GetMapping("/log/{logId}")
  public ResponseEntity<ApiResponse> getWorklogById(@PathVariable Long logId) {
    try {
      log.info("WorklogController: getWorklogById Execution Started");

      WorklogsDTO worklogsDTO = worklogsService.getWorklogById(logId);

      log.info("WorklogController: getWorklogById Execution Ended");
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponse<>("WorkLoges With WorkLog ID", worklogsDTO, "OK"));
    } catch (UserNotFoundException e) {
      log.error(
          "WorklogController: An error occurred while creating a worklog: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception e) {
      log.error("WorklogController: Error occurred while retrieving WorkLog by ID", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<>(
                  "Error Occurred While Retrieving WorkLog", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  //    @PostMapping("/tickets/{ticketId}")
  //    public ResponseEntity<Worklogs> addWorklogToTicket(@PathVariable Long ticketId, @RequestBody
  // Worklogs worklog) {
  //        Worklogs addedWorklog = worklogsService.addWorklogToTicket(ticketId, worklog);
  //        return new ResponseEntity<>(addedWorklog, HttpStatus.CREATED);
  //    }

  /**
   * Create a new worklog.
   *
   * @param worklog The worklog to create.
   * @return The created worklog.
   */
  @PostMapping
  public ResponseEntity<ApiResponse> createWorklog(@RequestBody Worklogs worklog) {
    try {
      // Perform validation if needed
      log.info("WorklogController: createWorklog Execution Started");
      WorklogsDTO worklogsDTO = worklogsService.createWorklog(worklog);
      log.info("WorklogController: createWorklog Exicution Ended: {}", worklogsDTO.getLog_id());
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(
              new ApiResponse<>(
                  " WorkLoges With WorkLog ID Created SuccesFully", worklogsDTO, "CREATED"));
    } catch (UserNotFoundException e) {
      log.error(
          "WorklogController: An error occurred while creating a worklog: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception e) {
      log.error(
          "WorklogController: An error occurred while creating a worklog: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<>(
                  "Error Occur  With Creating WorkLog ", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  /**
   * Update an existing worklog.
   *
   * @param logId The ID of the worklog to update.
   * @param worklog The updated worklog data.
   * @return The updated worklog.
   */
  @PutMapping("/log/{logId}")
  public ResponseEntity<ApiResponse> updateWorklog(
      @PathVariable Long logId, @RequestBody Worklogs worklog) {
    try {
      log.info("WorklogController: updateWorklog Execution Started");

      WorklogsDTO worklogsDTO = worklogsService.updateWorklog(logId, worklog);

      log.info("WorklogController: updateWorklog Execution Ended");
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new ApiResponse<>("WorkLog Updated Successfully", worklogsDTO, "CREATED"));
    } catch (UserNotFoundException e) {
      log.error(
          "WorklogController: An error occurred while creating a worklog: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception e) {
      log.error("WorklogController: Error occurred while updating WorkLog", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<>(
                  "Error Occurred While Updating WorkLog", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  /**
   * Delete a worklog by ID.
   *
   * @param logId The ID of the worklog to delete.
   * @return HTTP status indicating the success of the operation.
   */
  @DeleteMapping("/{logId}")
  public ResponseEntity<ApiResponse> deleteWorklog(@PathVariable Long logId) {
    try {
      log.info("WorklogController: deleteWorklog Execution Started");
      worklogsService.deleteWorklog(logId);
      log.info("WorklogController: deleteWorklog Execution Ended");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (WorklogNotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(
                      new ApiResponse<>(
                              e.getMessage(), null, "NOT_FOUND"));
    }catch (Exception e) {
      log.error("WorklogController: Error occurred while deleting WorkLog", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<>(
                  " Error Occur While Deleting WorkLog", null, "INTERNAL_SERVER_ERROR"));
    }
  }
}
