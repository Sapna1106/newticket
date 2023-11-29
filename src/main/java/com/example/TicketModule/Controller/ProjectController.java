package com.example.TicketModule.Controller;

import com.example.TicketModule.DTO.ApiResponse;
import com.example.TicketModule.DTO.ProjectDto;
import com.example.TicketModule.DTO.ProjectResponseDto;
import com.example.TicketModule.Entity.CustomField;
import com.example.TicketModule.Entity.Project;
import com.example.TicketModule.Exception.CustomeFieldNotFoundException;
import com.example.TicketModule.Exception.ProjectNotFoundException;
import com.example.TicketModule.Service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/projects")
public class ProjectController {
  private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
  @Autowired private ProjectService projectService;

  //    @PostMapping()
  //    public Project createProject(@RequestBody Project project) {
  //        return projectService.createProject(project);
  //    }

  //    @GetMapping
  //    public List<Project> getprojects() {
  //        return projectService.getAllProjects();
  //    }

  //    @GetMapping("/{id}")
  //    public Optional<Project> getProjectById(@PathVariable String id) {
  //        return projectService.getProjectById(id);
  //    }

  //    @DeleteMapping("/{id}")
  //    public void deleteProjectById(@PathVariable String id) {
  //        projectService.deleteProjectById(id);
  //    }

  @PutMapping("/{id}/customfields")
  public ResponseEntity<ApiResponse<?>> updateProjectWithCustomField(
      @PathVariable Long id, @RequestBody CustomField customField) {

    try {
      ProjectResponseDto projectResponseDto =
          projectService.updateProjectWithCustomField(id, customField);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new ApiResponse<>("CustomField Add to the Project", projectResponseDto, "created"));
    } catch (ProjectNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, "NOT_FOUND"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<>(
                  "Error occure while getting tickets by assigne Id",
                  false,
                  "INTERNAL_SERVER_ERROR"));
    }
  }

  @DeleteMapping("/{projectId}/customfields/{fieldId}")
  public ResponseEntity<?> deleteCustomField(
      @PathVariable Long projectId, @PathVariable Long fieldId) {
    try {
      projectService.deleteCustomField(projectId, fieldId);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponse<>("Custome Field Deleted SuccesFully", true, "created"));
    } catch (ProjectNotFoundException  | CustomeFieldNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), false, "NOT_FOUND"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ApiResponse<>("Error occure while getting tickets by assigne Id", false, "INTERNAL_SERVER_ERROR"));
    }
  }
}
