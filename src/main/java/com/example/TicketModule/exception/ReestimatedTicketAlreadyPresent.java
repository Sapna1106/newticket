package com.example.TicketModule.exception;

public class ReestimatedTicketAlreadyPresent extends RuntimeException{
    public ReestimatedTicketAlreadyPresent(String message) {
        super(message);
    }
}