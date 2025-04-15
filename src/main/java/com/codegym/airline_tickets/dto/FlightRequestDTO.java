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

    private String arrivalAirportOneWay;

    @NotNull
    private LocalDate departureTime;

    private LocalDate arrivalTime;

    private String sortProperty;

    private Integer price;

//    @NotEmpty
//    private int numberPassengers;

}
