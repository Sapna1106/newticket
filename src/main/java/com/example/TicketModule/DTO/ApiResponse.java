package com.example.TicketModule.DTO;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String message;
    private T data;
    private Long id;


    public ApiResponse(String message, Long id) {
        this.message = message;
        this.id = id;
    }
}

