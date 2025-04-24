package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByUserDto {
    private User userId;
    private BigInteger revenue;
}
