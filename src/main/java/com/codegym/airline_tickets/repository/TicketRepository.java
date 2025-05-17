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
    @Query(value = "SELECT t.* FROM tickets t ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
    Ticket findLatest();

    List<Ticket> findByBooking(Booking b);

    List<Ticket> findTicketByBookingId(Long bookingId);

    Ticket findByBookingIdAndFlightId(Long bookingId, Long flightId);

    @Query("SELECT t from Ticket t where t.flight.id = :flightId AND t.seat.id = :flightSeatId AND t.type = 1 ORDER BY t.id LIMIT 1")
    Ticket findByFlightIdAndSeatId(Long flightId, Long flightSeatId);
}
