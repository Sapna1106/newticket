package com.example.TicketModule.DTO;

import com.example.TicketModule.Entity.CustomField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectResponseDto {
    private Long id;
    private String projectName;

    private List<CustomField> customFieldList;

}
