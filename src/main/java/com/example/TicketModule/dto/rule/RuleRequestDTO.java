package com.example.TicketModule.dto.rule;

import java.util.List;
import lombok.Data;

@Data
public class RuleRequestDTO {

    private List<TriggerRequestDto> triggerRequestDtos;

    private List<ActionRequestDto> actionRequestDtos;

}