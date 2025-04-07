package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.User;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;

    private String code;

    private User user;

    private Flight flight;

    private Flight returnFlight;

    private Integer flightType;

    private Integer numTickets;

    private BigInteger totalPrice;

    private Integer status;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
