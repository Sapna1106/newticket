package com.example.TicketModule.exception;

public class StageNotFoundException extends RuntimeException{

    public StageNotFoundException() {
        super("Stage not found");
    }
    public StageNotFoundException(String message) {
        super(message);
    }
}