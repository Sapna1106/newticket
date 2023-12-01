package com.example.TicketModule.dto.rule;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomResponseEntity<T> {

    private String message;

    private int status;

    private T data;
}