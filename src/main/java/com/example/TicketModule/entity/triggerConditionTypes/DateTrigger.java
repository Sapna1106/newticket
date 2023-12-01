package com.example.TicketModule.entity.triggerConditionTypes;

import com.example.TicketModule.enums.ConditionOnTrigger;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Data;

@Data
@Entity
public class DateTrigger extends TriggerConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    private int days;

    private int hours;

    private int minuter;

    private Date date;

    @Override
    public ConditionOnTrigger getConditionType() {
        return ConditionOnTrigger.DATE;
    }
}
