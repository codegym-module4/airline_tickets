package com.codegym.airline_tickets.service;


import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Ticket;

import java.util.List;

public interface ITicketService extends IService<Ticket> {
    Ticket findLatest();

    Ticket updateOrCreate(Ticket t);

    List<Ticket> findByBooking(Booking booking);

    void deleteAll(List<Ticket> tickets);

    List<Ticket> findTicketByBookingId(Long bookingId);

    Ticket findByBookingIdAndFlightId(Long bookingId, Long flightId);

    Ticket findByFlightIdAndSeatId(Long flightId, Long seatId);

    Ticket findByCode(String code);
}
