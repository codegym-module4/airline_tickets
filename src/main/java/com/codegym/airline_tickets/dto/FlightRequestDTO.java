package com.codegym.airline_tickets.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightRequestDTO {
    private String type;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDate departureTime;
    private LocalDate arrivalTime;
    private String sortProperty;
}
