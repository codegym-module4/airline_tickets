package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.FlightSeat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDTO {
    private String code;
    private FlightResponseDTO flight;
    private FlightSeat seat;
    private String name;
    private String flightTime;
    private String timeToBoard;
}
