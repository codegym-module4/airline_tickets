package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailRequest {
    private String to;
    private String subject;
    //    private String message;
    private String name;
    private Account account;
}