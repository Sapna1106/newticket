package com.example.TicketModule.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "departmentId")
    private List<Project> projects;

    @OneToMany(mappedBy = "departmentId")
    private List<User> users;

    public Department(Long id) {
        this.id = id;
    }

}
