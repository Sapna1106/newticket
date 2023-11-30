package com.example.TicketModule.Dto;

import com.example.TicketModule.Dto.tickets.TicketResponseDto;
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
