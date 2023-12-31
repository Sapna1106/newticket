package com.example.TicketModule.Service;
import com.example.TicketModule.DTO.ProjectResponseDto;
import com.example.TicketModule.Entity.CustomField;
import com.example.TicketModule.Entity.Project;
import com.example.TicketModule.Exception.CustomFieldNameAlreadyPresent;
import com.example.TicketModule.Exception.CustomeFieldNotFoundException;
import com.example.TicketModule.Exception.ProjectNotFoundException;
import com.example.TicketModule.Repository.CustomFieldRepository;
import com.example.TicketModule.Repository.ProjectRepository;
import com.example.TicketModule.Repository.TicketRepository;
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

    public Project createProject(Project project) {
        return projectRepo.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepo.findById(id);
    }

    public void deleteProjectById(Long id) {
        projectRepo.deleteById(id);
    }

    public ProjectResponseDto updateProjectWithCustomField(Long projectId, CustomField customField){
         try{
             Optional<CustomField> customField1 = customFieldRepo.findByFieldNameAndProjectId(customField.getFieldName(), projectId);
//             System.out.println(customField1.get());
             if(!customField1.isPresent()){
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
             }else {
                 throw new CustomFieldNameAlreadyPresent("Custom field with this name is already present in present project");
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
