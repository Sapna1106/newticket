package com.example.TicketModule.dto.rule;


import com.example.TicketModule.entity.actionConditionType.*;
import com.example.TicketModule.enums.ConditionOnAction;
import lombok.Data;

@Data
public class ActionRequestDto {

    private Long actionFieldId;

    private ConditionOnAction actionCondition;

    private IdAction idAction;

    private NumberAction numberAction;

    private StringAction stringAction;

    private DateAction dateAction;

    private ProjectAction projectAction;

    private UserAction userAction;
}
