package com.example.TicketModule.entity;

import com.example.TicketModule.configration.JsonMapConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;

@Data
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

    private Long createdBy;

    private Date startDate;
    private Date endDate;
    private Date endTime;

    private Long projectId;

    private String status= "To Do";
    private String priority= "MEDIUM";

    private Long stageId;


    @ManyToMany
    @JoinTable(
            name = "ticket_users",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignee=new ArrayList<>();

    private Long accountableAssignee;


    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> customFields;

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

}
