package com.codegym.airline_tickets.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByDateDto {
    private LocalDate date;
    private BigInteger revenue;

    private List<RevenueByUserDto> revenueByUser;
    private List<RevenueByFlightDto> revenueByFlight;
    private List<RevenueByFlightTypeDto> revenueByFlightType;
}
