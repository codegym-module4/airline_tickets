package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByUserDto {
    private User userId;
    private Integer numberOfTickets;
    private LocalDate date;
    private BigInteger revenue;
}
