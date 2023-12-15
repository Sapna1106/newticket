package com.example.TicketModule.service;
import com.example.TicketModule.dto.ProjectResponseDto;
import com.example.TicketModule.entity.CustomField;
import com.example.TicketModule.entity.Project;
import com.example.TicketModule.exception.CustomeFieldNotFoundException;
import com.example.TicketModule.exception.ProjectNotFoundException;
import com.example.TicketModule.repository.CustomFieldRepository;
import com.example.TicketModule.repository.ProjectRepository;
import com.example.TicketModule.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private CustomFieldRepository customFieldRepo;
    @Autowired
    private TicketRepository ticketRepo;
    public ProjectResponseDto addCustomeField(Long projectId, CustomField customField){
         try{
             customField.setProjectId(projectId);
             customFieldRepo.save(customField);
             Optional<Project> projectOptional = projectRepo.findById(projectId);
             if(projectOptional.isPresent()){
                 Project project = projectOptional.get();
                 List<CustomField> customFieldList = customFieldRepo.findByProjectId(projectId);
                 ProjectResponseDto projectResponseDto = new ProjectResponseDto(project.getId(),project.getName(),customFieldList);
                 return projectResponseDto;
             }else {
                 throw new ProjectNotFoundException("Project With the Id Not Found");
             }
         }catch (Exception e){
             throw e;
         }
    }

    public Project addNewProject(Project newProject) {
        try {
            log.info("Adding a new project");
            return projectRepo.save(newProject);
        } catch (Exception e) {
            log.error("Error occurred while adding a new project: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<CustomField> getAllCustomField(Long projectId){
        return customFieldRepo.findByProjectId(projectId);
    }

    public Project getProjectById(Long projectId) {
        return projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
    }

    public boolean deleteCustomField(Long projectId, Long customFieldId){
        try{
            Optional<Project> projectOptional = projectRepo.findById(projectId);
            if(projectOptional.isPresent()){
                Optional<CustomField> customField = customFieldRepo.findById(customFieldId);
                if (customField.isPresent()){
                    customFieldRepo.deleteById(customFieldId);
                    return true;
                }
                else {
                    throw new CustomeFieldNotFoundException("Custome Filed With the Id Not Found");
                }
            }else {
                throw new ProjectNotFoundException("Project With the Id Not Found");
            }
        }catch (Exception e){
            throw e;
        }
    }

    public CustomField getCustomeFieldById(Long customeFieldId){
    return customFieldRepo
        .findById(customeFieldId)
        .orElseThrow(() -> new CustomeFieldNotFoundException("CustomField Not exist with the Id "+customeFieldId));
    }

    public CustomField getCustomFieldsByName(String name){
        return customFieldRepo.findByFieldName(name );
    }

    public List<CustomField> getCustomFieldByDataType(String dataType){
        return customFieldRepo.findByDataType(dataType);
    }

    public void addNonCustomeField(Long projectId) {
        CustomField nameField = new CustomField("name","STRING",projectId,"");
        CustomField startDateField = new CustomField("startDate","DATE",projectId,"");
        CustomField endDateField = new CustomField("endDate","DATE",projectId,"");
        CustomField statusField = new CustomField("status","STRING",projectId,"");
        CustomField stageIdField = new CustomField("stageId","STAGE",projectId,"");
        CustomField priorityField = new CustomField("priority","STRING",projectId,"");
        CustomField assigneeField = new CustomField("assignee","USER",projectId,"");
        CustomField accountableAssigneeField = new CustomField("accountableAssignee","USER",projectId,"");
        customFieldRepo.save(nameField);
        customFieldRepo.save(startDateField);
        customFieldRepo.save(endDateField);
        customFieldRepo.save(statusField);
        customFieldRepo.save(stageIdField);
        customFieldRepo.save(priorityField);
        customFieldRepo.save(assigneeField);
        customFieldRepo.save(accountableAssigneeField);
    }
}
