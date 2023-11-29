package com.example.TicketModule.Exception;

public class ReestimatedTicketAlreadyPresent extends RuntimeException{
    public ReestimatedTicketAlreadyPresent(String message) {
        super(message);
    }
}