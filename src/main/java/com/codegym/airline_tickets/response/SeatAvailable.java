package com.codegym.airline_tickets.response;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatAvailable {
    Integer flightId;
    String flightCode;
    Integer seatAvailable;

}
