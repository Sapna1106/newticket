package com.example.TicketModule.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customizedFields")
public class CustomizedField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String field_name;
    private String data_type;

    @ManyToOne
    @JoinColumn(name= "project_id")
    private Project projectId;

    private String description;
}
