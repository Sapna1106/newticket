package com.example.TicketModule.dto.tickets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String email;
}
