package com.example.TicketModule.dto.rule;

import com.example.TicketModule.entity.triggerConditionTypes.*;
import com.example.TicketModule.enums.ConditionOnTrigger;
import lombok.Data;

@Data
public class TriggerRequestDto {

    private Long triggerFieldId;

    private ConditionOnTrigger triggerCondition;

    private NumberTrigger numberTrigger;

    private StringTrigger stringTrigger;

    private DateTrigger dateTrigger;

    private StageTrigger stageTrigger;

    private UserTrigger userTrigger;

}
