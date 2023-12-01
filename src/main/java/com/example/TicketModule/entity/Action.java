package com.example.TicketModule.entity;

import com.example.TicketModule.entity.actionConditionType.ActionCondition;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "actions")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "action_field_id")
    private CustomField actionField;

    @OneToOne(cascade = CascadeType.ALL)
    private ActionCondition actionCondition;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Rule rule;

}
