//package com.example.TicketModule.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Rule {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long ruleId;
//
//  @OneToMany(mappedBy = "rule",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//  @JsonManagedReference
//  private List<Trigger> triggerList = new ArrayList<>();
//
//  @OneToMany(mappedBy = "rule",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//  @JsonManagedReference
//  private List<Action> actionList = new ArrayList<>();
//
//  @ManyToOne
//  @JoinColumn(name = "project_id")
//  @JsonBackReference
//  private Project project;
//}
