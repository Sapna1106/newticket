package com.example.TicketModule.service;
import com.example.TicketModule.entity.Project;
import com.example.TicketModule.entity.Stage;
import com.example.TicketModule.exception.StageNotFoundException;
import com.example.TicketModule.repository.ProjectRepository;
import com.example.TicketModule.repository.StageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StageService {
  private static final Logger logger = LoggerFactory.getLogger(StageService.class);

  @Autowired private StageRepo stageRepo;

  @Autowired private ProjectRepository projectRepo;

  /**
   * Get all stages for a project.
   *
   * @param projectId The ID of the project.
   * @return List of stages in the project.
   */
  public List<Stage> getAllStages(long projectId) {
    try {
    List<Stage> stageList = stageRepo.findByProjectId(projectId);
    return stageList;
    } catch (Exception e) {
      logger.error(
          "Error occurred while retrieving stages for project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Get a stage by its ID for a project.
   *
   * @param projectId The ID of the project.
   * @param stageId The ID of the stage.
   * @return The stage with the given ID.
   */
  public Stage getStageById(long projectId, long stageId) {
    try {
        Optional<Stage> stageOptional = stageRepo.findByIdAndProjectId(stageId,projectId);
        if (stageOptional.isPresent()) {
          logger.info("Stage with ID {} found in project with ID {}", stageId, projectId);
          return stageOptional.get();
        } else {
          throw new StageNotFoundException("Stage with ID " + stageId + " not found");
        }
    } catch (Exception e) {
      logger.error(
          "Error occurred while retrieving stage with ID {} in project with ID {}: {}",
          stageId,
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Add a new stage to a project.
   *
   * @param projectId The ID of the project.
   * @param stage The stage to be added.
   * @return The added stage.
   */
  public Stage addNewStage(long projectId, Stage stage) {
    try {
        stage.setProjectId(projectId);
        logger.info("Stage added successfully");
        return stageRepo.save(stage);
    } catch (Exception e) {
      logger.error(
          "Error occurred while adding a new stage to project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Update a stage in a project.
   *
   * @param projectId The ID of the project.
   * @param updateStage The updated stage.
   * @return The updated stage.
   */
  public Stage updateStage(long projectId, Stage updateStage) {
    try {
        Optional<Stage> stageOptional = stageRepo.findById(updateStage.getId());
        if (stageOptional.isPresent()) {
          Stage existingStage = stageOptional.get();
          existingStage.setStageName(updateStage.getStageName());
          existingStage.setStageDescription(updateStage.getStageDescription());
          logger.info("Stage updated successfully");
          return stageRepo.save(existingStage);
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + updateStage.getId() + " not found");
        }
    } catch (Exception e) {
      logger.error(
          "Error occurred while updating stage in project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Delete a stage from a project.
   *
   * @param projectId The ID of the project.
   * @param stageId The ID of the stage to be deleted.
   */
  public void deleteStageById(long projectId, long stageId) {
    try {
        Optional<Stage> stageOptional = stageRepo.findById(stageId);
        if (stageOptional.isPresent()) {
          stageRepo.deleteById(stageId);

          logger.info("Stage deleted successfully");
        } else {
          throw new StageNotFoundException("Stage with ID " + stageId + " not found");
        }
    } catch (Exception e) {
      logger.error(
          "Error occurred while deleting stage in project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

//  public void reorderStage(long projectId, int sourceIndex, int destinationIndex) {
//    Optional<Project> projectOptional = projectRepo.findById(projectId);
//    if (projectOptional.isPresent()) {
//      Project project = projectOptional.get();
//      List<Stage> stageList = project.getStageList();
//
//      Stage stageToMove = stageList.remove(sourceIndex);
//      stageList.add(destinationIndex, stageToMove);
//      projectRepo.save(project);
//    }
//  }
}
