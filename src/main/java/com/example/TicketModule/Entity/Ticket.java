package com.example.TicketModule.Entity;

import com.example.TicketModule.DTO.RequestBodyTicket;
import com.example.TicketModule.Enum.Priority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ticketId;
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    private Instant startDate;
    private Instant endDate;
    private Instant endTime;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project projectId;

    private String status= "To Do";
    private Priority priority= Priority.MEDIUM;
//    private String blockedBy;

//    @ManyToOne
//    @JoinColumn(name = "parent_ticket_id")
//    private Ticket parent;

    @ManyToMany
    @JoinTable(
            name = "ticket_users",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignee;

    @ManyToOne
    @JoinColumn(name = "accountable_assignee_id")
    private User accountableAssignee;

    private String customFields;

    @ManyToMany
    @JoinTable(
            name = "ticket_references",
            joinColumns = @JoinColumn(name = "source_ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "target_ticket_id")
    )
    private Set<Ticket> referencedTickets;

    @ManyToMany(mappedBy = "referencedTickets")
    private Set<Ticket> referencingTickets;

//    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<OtherLinks> otherLinks;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public Ticket(RequestBodyTicket requestBodyTicket) {
        this.id = requestBodyTicket.getId();
        this.ticketId = requestBodyTicket.getTicketId();
        this.name = requestBodyTicket.getName();
        this.description = requestBodyTicket.getDescription();
        this.createdBy = new User(); // jay id
        this.startDate = Instant.now();
        this.endDate = requestBodyTicket.getEndDate();
        this.endTime = requestBodyTicket.getEndTime();
        this.projectId = new Project(requestBodyTicket.getProjectId());
        this.status = requestBodyTicket.getStatus();
        this.priority = Priority.valueOf(requestBodyTicket.getPriority().toUpperCase());
        this.customFields = requestBodyTicket.getCustomFields();
//        this.accountableAssignee = requestBodyTicket.getAccountableAssignee();
        this.assignee = requestBodyTicket.getAssignee().stream()
                .map(User::new)
                .collect(Collectors.toList());
    }

    public void generateCustomId(String projectInitials, long count) {
        this.ticketId = projectInitials + count;
    }

}
