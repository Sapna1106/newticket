package com.example.TicketModule.dto;

import com.example.TicketModule.dto.tickets.TicketResponseDto;
import com.example.TicketModule.entity.PlanedTask;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanedTaskDto {
    private Long id;

    private Long userId;

    private TicketResponseDto ticket;

    private LocalDateTime dropDate;
    private LocalDateTime startTicketTime;
    private LocalDateTime endTicketTime;

    public PlanedTaskDto(PlanedTask planedTask){
        this.id=planedTask.getId();
        this.dropDate=planedTask.getDropDate();
        this.endTicketTime=planedTask.getEndTicketTime();
        this.startTicketTime=planedTask.getStartTicketTime();
        this.userId=planedTask.getUserId();
        this.dropDate=planedTask.getDropDate();
    }

}
