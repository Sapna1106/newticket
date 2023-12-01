package com.example.TicketModule.dto.reestimated;

import com.example.TicketModule.dto.tickets.UserDto;
import com.example.TicketModule.entity.ReestimatedTicket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReestimatedTicketResponseDTO {

    private Long id;
    private Long ticketId;
    private String reason;
    private String denyReason;
    private String  status;
    private Date newDate;
    private UserDto reestimatedByName;
    private UserDto assignedToName;

    public ReestimatedTicketResponseDTO(ReestimatedTicket reestimatedTicket) {
        this.id = reestimatedTicket.getId();
        this.ticketId = reestimatedTicket.getTicket().getId();
        this.status=reestimatedTicket.getStatus();
        this.reason = reestimatedTicket.getReason();
        this.denyReason = reestimatedTicket.getDenyReason();
        this.newDate = reestimatedTicket.getNewDate();
    }


}