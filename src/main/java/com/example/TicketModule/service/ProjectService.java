package com.example.TicketModule.service;
import com.example.TicketModule.Dto.ProjectResponseDto;
import com.example.TicketModule.entity.CustomField;
import com.example.TicketModule.entity.Project;
import com.example.TicketModule.exception.CustomeFieldNotFoundException;
import com.example.TicketModule.exception.ProjectNotFoundException;
import com.example.TicketModule.repository.CustomFieldRepository;
import com.example.TicketModule.repository.ProjectRepository;
import com.example.TicketModule.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private CustomFieldRepository customFieldRepo;
    @Autowired
    private TicketRepository ticketRepo;
    public ProjectResponseDto updateProjectWithCustomField(Long projectId, CustomField customField){
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
}
