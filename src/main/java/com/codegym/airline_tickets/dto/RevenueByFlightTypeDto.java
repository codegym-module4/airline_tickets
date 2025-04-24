package com.codegym.airline_tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RevenueByFlightTypeDto {
    private Integer flightType;
    private BigInteger revenue;
}
