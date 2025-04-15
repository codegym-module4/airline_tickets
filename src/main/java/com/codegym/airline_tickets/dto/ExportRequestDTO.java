package com.codegym.airline_tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportRequestDTO {
    private List<RevenueByDateDto> totalRevenue;
    private String chartImage;
    private BigInteger totalSumRevenue;
    private BigInteger totalSumCompareRevenue;
}
