package com.codegym.airline_tickets.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightRequestDTO {
    private String type;

    @NotNull
    @NotEmpty
    private String departureAirport;

    @NotNull
    @NotEmpty
    private String arrivalAirport;

    @NotNull
    @NotEmpty
    private String arrivalAirportOneWay;

    @NotNull
    private LocalDate departureTime;

    @NotNull
    private LocalDate arrivalTime;

    private String sortProperty = "price";

//    @NotEmpty
//    private int numberPassengers;

}
