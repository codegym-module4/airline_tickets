package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RevenueByFlightDto {
    private Flight flightId;
    private Flight returnFlightId;
    private BigInteger revenue;
}
