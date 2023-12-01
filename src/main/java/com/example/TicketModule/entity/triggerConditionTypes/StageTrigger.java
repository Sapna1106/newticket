package com.example.TicketModule.entity.triggerConditionTypes;

import com.example.TicketModule.enums.ConditionOnTrigger;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StageTrigger extends TriggerConditions{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    private Long previousStage;

    private Long currentStage;

    @Override
    public ConditionOnTrigger getConditionType() {
        return ConditionOnTrigger.STAGE;
    }
}
