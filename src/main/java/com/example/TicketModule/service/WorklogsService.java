package com.example.TicketModule.service;
import com.example.TicketModule.Dto.tickets.UserDto;
import com.example.TicketModule.Dto.WorklogTicketDto;
import com.example.TicketModule.Dto.WorklogsDTO;
import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.entity.User;
import com.example.TicketModule.entity.Worklogs;
import com.example.TicketModule.exception.UserNotFoundException;
import com.example.TicketModule.exception.WorklogNotFoundException;
import com.example.TicketModule.repository.TicketRepository;
import com.example.TicketModule.repository.UserRepository;
import com.example.TicketModule.repository.WorklogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorklogsService {
  @Autowired
  private WorklogRepository worklogRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TicketRepository ticketRepository;

  public List<WorklogsDTO> getAllWorklogs() {
    try {
      log.info("WorklogsService: getAllWorklogs Execution Started");

      List<Worklogs> worklogs = worklogRepository.findAll();
      List<WorklogsDTO> worklogsDTOs = worklogs.stream()
              .map(worklog -> {
                try {
                  WorklogsDTO worklogsDTO = new WorklogsDTO(worklog);
                  convetToUser(worklogsDTO, worklog);
                  return worklogsDTO;
                } catch (Exception e) {
                  log.error("Error occurred while processing worklog: " + e.getMessage(), e);
                  return null;
                }
              })
              .collect(Collectors.toList());

      log.info("WorklogsService: getAllWorklogs Execution Ended");
      return worklogsDTOs;
    } catch (Exception e) {
      log.error("WorklogsService: Error occurred while fetching all worklogs.", e);
      throw e;
    }
  }

  public WorklogsDTO getWorklogById(Long logId) {
    try {
      log.info("WorklogsService: getWorklogById Execution Started");

      Worklogs worklogs = worklogRepository.findById(logId)
              .orElseThrow(() -> new WorklogNotFoundException("Worklog not found"));

      WorklogsDTO worklogsDTO = new WorklogsDTO(worklogs);
      convetToUser(worklogsDTO, worklogs);

      log.info("WorklogsService: getWorklogById Execution Ended");
      return worklogsDTO;
    } catch (Exception e) {
      log.error("WorklogsService: Error occurred while fetching worklog by ID: " + logId, e);
      throw e;
    }
  }

  public WorklogsDTO createWorklog(Worklogs worklog) {
    try {
      log.info("WorklogsService: createWorklog Execution Started");

      LocalDate currentDate = LocalDate.now();
      worklog.setDate(currentDate.atStartOfDay());

      Worklogs worklogs = worklogRepository.save(worklog);
      WorklogsDTO worklogsDTO = new WorklogsDTO(worklogs);
      convetToUser(worklogsDTO, worklogs);

      log.info("WorklogsService: createWorklog Execution Ended");
      return worklogsDTO;
    } catch (Exception e) {
      log.error("WorklogsService: Error occurred while creating worklog: " + e.getMessage(), e);
      throw e;
    }
  }

  public WorklogsDTO updateWorklog(Long logId, Worklogs updatedWorklog) {
    try {
      log.info("WorklogsService: updateWorklog Execution Started");

      if (!worklogRepository.existsById(logId)) {
        throw new WorklogNotFoundException("Worklog not found");
      }

      updatedWorklog.setLog_id(logId);
      Worklogs worklogs = worklogRepository.save(updatedWorklog);
      WorklogsDTO worklogsDTO = new WorklogsDTO(worklogs);
      convetToUser(worklogsDTO, worklogs);

      log.info("WorklogsService: updateWorklog Execution Ended");
      return worklogsDTO;
    } catch (Exception e) {
      log.error("WorklogsService: Error occurred while updating worklog with ID: " + logId, e);
      throw e;
    }
  }

  public void deleteWorklog(Long logId) {
    try {
      log.info("WorklogsService: deleteWorklog Execution Started");

      if (!worklogRepository.existsById(logId)) {
        throw new WorklogNotFoundException("Worklog not found");
      }

      worklogRepository.deleteById(logId);

      log.info("WorklogsService: deleteWorklog Execution Ended");
    } catch (Exception e) {
      log.error("WorklogsService: Error occurred while deleting worklog with ID: " + logId, e);
      throw e;
    }
  }

  public List<WorklogsDTO> getWorklogsByTicketId(Long ticket_id) {
    try {
      log.info("WorklogsService: getWorklogsByTicketId Execution Started");

      List<Worklogs> worklogs = worklogRepository.findByTicketId(ticket_id);
      if (worklogs.isEmpty()) {
        throw new EntityNotFoundException("No worklogs found for ticket with ID: " + ticket_id);
      }
      List<WorklogsDTO> worklogsDTOs = worklogs.stream()
              .map(worklog -> {
                try {
                  WorklogsDTO worklogsDTO = new WorklogsDTO(worklog);
                  convetToUser(worklogsDTO, worklog);
                  return worklogsDTO;
                } catch (Exception e) {
                  log.error("Error occurred while processing worklog: " + e.getMessage(), e);
                  return null;
                }
              })
              .collect(Collectors.toList());

      log.info("WorklogsService: getWorklogsByTicketId Execution Ended");
      return worklogsDTOs;
    } catch (Exception e) {
      log.error("WorklogsService: Error occurred while fetching worklogs by ticket ID: " + ticket_id, e);
      throw e;
    }
  }

  public void convetToUser(WorklogsDTO worklogsDTO, Worklogs worklogs) {
    try {
      log.info("WorklogsService: convetToUser Execution Started");

      Optional<User> userOptional = userRepository.findById(worklogs.getUserId());
      if (userOptional.isPresent()) {
        worklogsDTO.setUserDto(
                new UserDto(
                        userOptional.get().getId(),
                        userOptional.get().getUserName(),
                        userOptional.get().getEmail()));
      } else {
        throw new UserNotFoundException("User With the Id do not exist ");
      }
      Optional<Ticket> ticketOptional = ticketRepository.findById(worklogs.getTicketId());
      if (ticketOptional.isPresent()) {
        worklogsDTO.setWorklogTicketDto(new WorklogTicketDto(ticketOptional.get().getId(), ticketOptional.get().getName()));
      }

      log.info("WorklogsService: convetToUser Execution Ended");
    } catch (Exception e) {
      log.error("WorklogsService: Error occurred in convetToUser method: " + e.getMessage(), e);
      throw e;
    }
  }
}
