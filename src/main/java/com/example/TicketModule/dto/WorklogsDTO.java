package com.example.TicketModule.dto;

import java.time.LocalDateTime;

import com.example.TicketModule.dto.tickets.UserDto;
import com.example.TicketModule.entity.Worklogs;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorklogsDTO {
    private Long log_id;
    private LocalDateTime date;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private int duration;
    private WorklogTicketDto worklogTicketDto;

    private UserDto userDto;

    private WorklogsDTO mapWorklogsToDTO(Worklogs worklogs) {
        WorklogsDTO worklogsDTO = new WorklogsDTO();

        worklogsDTO.setLog_id(worklogs.getLog_id());
        worklogsDTO.setDate(worklogs.getDate());
        worklogsDTO.setStart_time(worklogs.getStart_time());
        worklogsDTO.setEnd_time(worklogs.getEnd_time());
        worklogsDTO.setDuration(worklogs.getDuration());
        return worklogsDTO;
    }
    public WorklogsDTO(Worklogs worklogs){
        this.log_id = worklogs.getLog_id();
        this.date = worklogs.getDate();
        this.start_time = worklogs.getStart_time();
        this.end_time = worklogs.getEnd_time();
        this.duration = worklogs.getDuration();
    }
}

