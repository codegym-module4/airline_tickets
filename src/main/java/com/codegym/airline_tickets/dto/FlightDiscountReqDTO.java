package com.codegym.airline_tickets.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightDiscountReqDTO {
    @NotNull
    @NotEmpty
    private String departureAirport;

    @NotNull
    @NotEmpty
    private String arrivalAirport;

    @NotNull
    @NotEmpty
    private int price;

    private String sortProperty = "price";
}
