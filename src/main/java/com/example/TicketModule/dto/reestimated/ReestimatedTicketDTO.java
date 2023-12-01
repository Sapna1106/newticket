package com.example.TicketModule.dto.reestimated;

import com.example.TicketModule.entity.ReestimatedTicket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReestimatedTicketDTO {

    private Long id;
    private Long ticketId;
    private String reason;
    private String denyReason;
    private String  status;
    private String newDate;
    private String reestimatedByName;
    private String assignedToName;

    public ReestimatedTicketDTO(ReestimatedTicket reestimatedTicket) {
        this.id = reestimatedTicket.getId();
        this.ticketId = reestimatedTicket.getTicket().getId();
        this.status=reestimatedTicket.getStatus();
        this.reason = reestimatedTicket.getReason();
        this.denyReason = reestimatedTicket.getDenyReason();
        this.newDate = reestimatedTicket.getNewDate().toString();
//        this.reestimatedByName = reestimatedTicket.getReestimatedBy().getFirstName();
//        this.assignedToName = reestimatedTicket.getAssignedTo().getFirstName();
    }

    @Override
    public String toString() {
        return "ReestimatedTicketDTO{" +
                "id='" + id + '\'' +
                ", ticketId='" + ticketId + '\'' +
                ", reason='" + reason + '\'' +
                ", denyReason='" + denyReason + '\'' +
                ", newDate='" + newDate + '\'' +
                ", reestimatedByName='" + reestimatedByName + '\'' +
                ", assignedToName='" + assignedToName + '\'' +
                '}';
    }

//    public void setAssignedTo(User assignedTo) {
//        this.assignedToName = assignedTo.getFirstName();
//    }
//    public String getAssignedTo() {
//        return assignedToName;
//    }
//    public void setReestimatedBy(User reestimatedBy) {
//        this.reestimatedByName = reestimatedBy.getFirstName();
//    }
//    public String getReestimatedBy() {
//        return reestimatedByName;
//    }

}
