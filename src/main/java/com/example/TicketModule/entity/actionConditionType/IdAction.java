package com.example.TicketModule.entity.actionConditionType;

import com.example.TicketModule.enums.ConditionOnAction;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class IdAction extends ActionCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long newStageId;

    @Override
    public ConditionOnAction getConditionType() {
        return ConditionOnAction.ID;
    }
}
