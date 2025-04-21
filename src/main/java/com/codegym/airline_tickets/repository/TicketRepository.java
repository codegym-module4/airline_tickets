package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Seat;
import com.codegym.airline_tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t ORDER BY t.id DESC LIMIT 1")
    Ticket findLatest();

    List<Ticket> findByBooking(Booking b);

    List<Ticket> findTicketByBookingId(Long bookingId);

    Ticket findByBookingIdAndFlightId(Long bookingId, Long flightId);
}
