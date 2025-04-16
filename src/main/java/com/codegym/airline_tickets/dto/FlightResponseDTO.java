package com.codegym.airline_tickets.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightResponseDTO implements Serializable {
   private Long id;
   private String airlineName;
   private String flightCode;
   private LocalDateTime departureTime;
   private LocalDateTime arrivalTime;
   private String price;
   private String departureAirportCity;
   private String arrivalAirportCity;
   private String departureAirportName;
   private String arrivalAirportName;
   private String priceVATTotal;
   private String priceVAT;
   private Integer seatAvailability;
}
