package com.example.TicketModule.entity.actionConditionType;

import com.example.TicketModule.entity.User;
import com.example.TicketModule.enums.ConditionOnAction;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserAction extends ActionCondition{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    @JoinColumn(name = "user_Id")
    @ManyToOne
    private User userAction;

    @Override
    public ConditionOnAction getConditionType() {
        return ConditionOnAction.USER;
    }
}
