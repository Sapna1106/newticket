package com.example.TicketModule.DTO;

import com.example.TicketModule.Entity.ReestimatedTicket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReestimatedTicketResponseDTO {

    private Long id;
    private Long ticketId;
    private String reason;
    private String denyReason;
    private String  status;
    private String newDate;
    private UserDto reestimatedByName;
    private UserDto assignedToName;

    public ReestimatedTicketResponseDTO(ReestimatedTicket reestimatedTicket) {
        this.id = reestimatedTicket.getId();
        if(reestimatedTicket.getTicket() != null)
            this.ticketId = reestimatedTicket.getTicket().getId();
        this.status=reestimatedTicket.getStatus();
        this.reason = reestimatedTicket.getReason();
        this.denyReason = reestimatedTicket.getDenyReason();
        this.newDate = reestimatedTicket.getNewDate().toString();
    }


}