package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.User;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class BookingDTO {
    List<BookingTicketDTO> items;
}
