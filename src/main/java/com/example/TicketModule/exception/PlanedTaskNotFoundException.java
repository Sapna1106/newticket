package com.example.TicketModule.exception;

public class PlanedTaskNotFoundException extends RuntimeException{
    public PlanedTaskNotFoundException(String message) {
        super(message);
    }
}
