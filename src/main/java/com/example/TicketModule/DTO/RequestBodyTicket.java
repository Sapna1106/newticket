package com.example.TicketModule.DTO;

import com.example.TicketModule.Entity.Project;
import com.example.TicketModule.Entity.User;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class RequestBodyTicket {
    private  Long id;
    private String ticketId;
    private String name;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private Instant endTime;
    private Long createdBy;
    private Long projectId;
    private String status;
    private String priority;

//    private String blockedBy;
//    private String parent;

    private List<Long> assignee;
    private Long accountableAssignee;
    private String customFields;

    public RequestBodyTicket() {
    }

    public RequestBodyTicket(Long id, String ticketId, String name, String description, Instant startDate, Instant endDate, Instant endTime, Long createdBy, Long projectId, String status, String priority, List<Long> assignee, Long accountableAssignee, String customFields) {
        this.id = id;
        this.ticketId = ticketId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.endTime = endTime;
        this.createdBy = createdBy;
        this.projectId = projectId;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.accountableAssignee = accountableAssignee;
        this.customFields = customFields;
    }

    //    private List<OtherLinks> otherLinks;

    @Override
    public String toString() {
        return "RequestBodyTicket{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", endTime='" + endTime + '\'' +
                ", projectIn='" + projectId + '\'' +
                ", stage='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", createdBy='" + createdBy + '\'' +
//                ", blockedBy='" + blockedBy + '\'' +
//                ", parent='" + parent + '\'' +
                ", assignee=" + assignee +
                ", accountableAssignee=" + accountableAssignee +
                ", customFields=" + customFields +
//                ", otherLinks=" + otherLinks +
                '}';
    }

}
