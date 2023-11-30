package com.example.TicketModule.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomField> customizedFields;

    @ManyToOne
    @JoinColumn(name= "department_id")
    private Department departmentId;

    public Project(Long projectId) {
    }

    public String getInitials() {
        if (name != null && !name.isEmpty()) {
            return Arrays.stream(this.name.split("\\s+"))
                    .filter(word -> !word.isEmpty())
                    .map(word -> String.valueOf(word.charAt(0)))
                    .collect(Collectors.joining());
        } else {
            return "";
        }
    }
}
