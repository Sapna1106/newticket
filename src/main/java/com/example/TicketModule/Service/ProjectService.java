//package com.example.TicketModule.Service;
//
//import com.example.TicketModule.Entity.CustomizedField;
//import com.example.TicketModule.Entity.Project;
//import com.example.TicketModule.Entity.Ticket;
//import com.example.TicketModule.Exception.ResourceNotFoundException;
//import com.example.TicketModule.Repository.CustomizedRepository;
//import com.example.TicketModule.Repository.ProjectRepository;
//import com.example.TicketModule.Repository.TicketRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class ProjectService {
//    @Autowired
//    private ProjectRepository projectRepo;
//
//    @Autowired
//    private CustomizedRepository customizedFiledRepo;
//    @Autowired
//    private TicketRepository ticketRepo;
//    public Project createProject(Project project) {
//        return projectRepo.save(project);
//    }
//
//    public List<Project> getAllProjects() {
//        return projectRepo.findAll();
//    }
//
//    public Optional<Project> getProjectById(Long id) {
//        return projectRepo.findById(id);
//    }
//
//    public void deleteProjectById(Long id) {
//        projectRepo.deleteById(id);
//    }
//    public Project updateProjectWithCustomField(Long id, CustomizedField customizedFiled){
//
//        Optional<Project> project =projectRepo.findById(id);
//        List<CustomizedField> customizedFileds=project.get().getCustomizedFields();
//        if(customizedFileds==null){
//            customizedFiled= customizedFiledRepo.save(customizedFiled);
//            customizedFileds= new ArrayList<>();
//            customizedFileds.add(customizedFiled);
//        }else {
//
//            List<CustomizedField> finalCustomizedFileds = customizedFileds;
//            CustomizedField finalCustomizedFiled = customizedFiled;
//            boolean nameExists = finalCustomizedFileds.stream()
//                    .anyMatch(existingField -> existingField.getName().trim().equals(finalCustomizedFiled.getName().trim()));
//
//            if (!nameExists) {
//                customizedFiled= customizedFiledRepo.save(customizedFiled);
//
//                customizedFileds.add(customizedFiled);
//            } else {
//                throw new IllegalArgumentException("CustomizedField with the same name already exists.");
//            }
//        }
//
//
//        project.get().setCustomizedFields(customizedFileds);
//        System.out.println(project);
//        return  projectRepo.save(project.get());
//    }
//    public Project deleteCustomField(Long projectId, Long fieldId) {
//        Optional<Project> optionalProject = projectRepo.findById(projectId);
//        Optional<CustomizedField> customizedFiled= customizedFiledRepo.findById(fieldId);
//        if (optionalProject.isPresent()) {
//            Project project = optionalProject.get();
//
//
//            project.getCustomizedFields().removeIf(customField -> customField.getId().equals(fieldId));
//
//
//            customizedFiledRepo.deleteById(fieldId);
//
//            List<Ticket> tickets = ticketRepo.findByProjectId_Id(projectId);
//
//
//
//            tickets.forEach(ticket -> {
//                String customFields = ticket.getCustomFields();
//                customFields.entrySet().removeIf(entry -> entry.getKey().equals(customizedFiled.get().getName()));
//            });
//            ticketRepo.saveAll(tickets);
//            System.out.println(tickets);
//            return projectRepo.save(project);
//        } else {
//            throw new ResourceNotFoundException("Project not found with id: " + projectId);
//        }
//    }
//}
