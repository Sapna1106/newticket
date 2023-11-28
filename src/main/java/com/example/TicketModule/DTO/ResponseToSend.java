package com.example.TicketModule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseToSend<T> {
    private String message;
    private T data;
    private HttpStatus status;


}
