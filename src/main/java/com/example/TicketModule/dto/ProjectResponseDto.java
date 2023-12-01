package com.example.TicketModule.dto;

import com.example.TicketModule.entity.CustomField;
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
