package com.example.TicketModule.service;

import com.example.TicketModule.dto.tickets.TicketResponseDto;
import com.example.TicketModule.entity.Ticket;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Data
public class ResponseWebSocket {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public void sendResponse(Ticket ticket){
    TicketResponseDto ticketResponseDto = new TicketResponseDto(ticket);
        messagingTemplate.convertAndSend("/topic/ticket-updates",  ticketResponseDto);
    }
    public void sendMessaage(Long ruleId){
        messagingTemplate.convertAndSend("/topic/ticket-message",ruleId);
    }
}
