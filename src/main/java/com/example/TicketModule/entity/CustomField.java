package com.example.TicketModule.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CustomField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fieldName;
    private String dataType;

    private Long projectId;

    private String description;

    public CustomField(String fieldName,String dataType,Long projectId,String description){
        this.fieldName=fieldName;
        this.dataType=dataType;
        this.projectId=projectId;
        this.description=description;
    }
}
