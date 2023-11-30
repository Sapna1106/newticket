package com.example.TicketModule.controller;

import com.example.TicketModule.Dto.ApiResponse;
import com.example.TicketModule.Dto.PlanedTaskDto;
import com.example.TicketModule.entity.PlanedTask;
import com.example.TicketModule.exception.PlanedTaskNotFoundException;
import com.example.TicketModule.exception.TicketNotFoundException;
import com.example.TicketModule.service.PlanedTaskService;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ticket-drops")
@Slf4j
public class PlanedTaskController {

  @Autowired
  private PlanedTaskService userTicketService;

  @PostMapping("/")
  public ResponseEntity<ApiResponse> createPlanedTask(@RequestBody PlanedTask userTicketData) {
    try {
      log.info("PlanedTaskController: createPlanedTask execution started");
      PlanedTaskDto planedTaskDto = userTicketService.createPlanedTask(userTicketData);
      log.info("PlanedTaskController: createPlanedTask execution ended");
      return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Plane Created Successfully", planedTaskDto, "CREATED"));
    }catch (TicketNotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null, "NOT_FOUND"));
    }
    catch (Exception e) {
      log.error("PlanedTaskController: createPlanedTask error occurred while adding the plane", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to create user ticket", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  /**
   *
   * @param userId
   * @param ticketId
   * @param newUserTicketData
   * @return
   */
  @PutMapping("/{userId}/{ticketId}")
  public ResponseEntity<ApiResponse> updatePlanedTask(
          @PathVariable Long userId,
          @PathVariable Long ticketId,
          @RequestBody PlanedTask newUserTicketData) {
    try {
      log.info("PlanedTaskController: updatePlanedTask execution started");
      PlanedTask updatedUserTicket =
              userTicketService.updatePlanedTask(userId, ticketId, newUserTicketData);
      log.info("PlanedTaskController: updatePlanedTask execution ended");
      return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Plane Updated Successfully", updatedUserTicket, "CREATED"));
    } catch (TicketNotFoundException e) {
      log.error("PlanedTaskController: updatePlanedTask TicketNotFoundException - ticket with the given ID not found", e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Ticket with the ID not found", null, "NOT_FOUND"));
    } catch (Exception e) {
      log.error("PlanedTaskController: updatePlanedTask error occurred while updating the plane", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to update user ticket", null, "INTERNAL_SERVER_ERROR"));
    }
  }

  @DeleteMapping("/{userId}/{ticketId}")
  public ResponseEntity<ApiResponse> deletePlanedTask(
          @PathVariable Long userId, @PathVariable Long ticketId) {
    try {
      log.info("PlanedTaskController: deletePlanedTask execution started");
      userTicketService.deletePlanedTask(userId, ticketId);
      log.info("PlanedTaskController: deletePlanedTask execution ended");
      return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Plane Deleted Successfully", true, "OK"));
    }catch (PlanedTaskNotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, "NOT_FOUND"));
    }
    catch (Exception e) {
      log.error("PlanedTaskController: deletePlanedTask error occurred while deleting the plane", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to delete plane", false, "INTERNAL_SERVER_ERROR"));
    }
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getAllPlanedTask(@PathVariable Long userId) {
    try {
      log.info("PlanedTaskController: getAllPlanedTask execution started");
      List<PlanedTaskDto> planedTaskDtos = userTicketService.getAllPlanedTask(userId);
      log.info("PlanedTaskController: getAllPlanedTask execution ended");
      return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("List of Planes", planedTaskDtos, "OK"));
    } catch (Exception e) {
      log.error("PlanedTaskController: getAllPlanedTask error occurred while getting the list of planes", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to get the list of planes", null, "INTERNAL_SERVER_ERROR"));
    }
  }
}
