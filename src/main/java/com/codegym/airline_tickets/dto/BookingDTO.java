package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private Long id;

    private Flight flight;

    private Flight returnFlight;

    private Integer flightType;

    private Integer numberOfTickets;

    private BigInteger totalPrice;
    @Valid
    private List<BookingTicketDTO> items;
}
