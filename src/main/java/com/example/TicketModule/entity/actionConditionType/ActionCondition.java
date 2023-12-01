package com.example.TicketModule.entity.actionConditionType;

import com.example.TicketModule.enums.ConditionOnAction;
import jakarta.persistence.*;

@Entity
@Table(name = "action_condition")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "condition_type")
public abstract class ActionCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract ConditionOnAction getConditionType();
}
