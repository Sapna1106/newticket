package com.example.TicketModule.exception;

public class RuleNotFoundException extends RuntimeException {

    public RuleNotFoundException() {
        super("Rule Not Found");
    }

    public RuleNotFoundException(String message) {
        super(message);
    }
}
