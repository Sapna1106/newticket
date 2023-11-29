package com.example.TicketModule.Exception;

public class CustomFieldNameAlreadyPresent extends RuntimeException{
    public CustomFieldNameAlreadyPresent(String message) {
        super(message);
    }
}
