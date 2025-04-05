package com.codegym.airline_tickets.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDTO implements Serializable {
    String airlineName;
    String flightCode;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    BigInteger price;
}
