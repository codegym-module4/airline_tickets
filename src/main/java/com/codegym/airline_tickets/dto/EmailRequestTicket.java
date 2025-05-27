package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailRequestTicket {
    private String to;
    private String subject;
    private TicketResponseDTO ticket;
}
