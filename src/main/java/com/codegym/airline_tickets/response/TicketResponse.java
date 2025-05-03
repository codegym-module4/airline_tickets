package com.codegym.airline_tickets.response;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.dto.TicketResponseDTO;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.FlightSeat;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private boolean errors = false;
    private HttpStatus status;
    private String message;
    private TicketResponseDTO ticketResponseDTO;
    private String html;
}
