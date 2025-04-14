package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Seat;
import com.codegym.airline_tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t ORDER BY t.id DESC LIMIT 1")
    Ticket findLatest();
}
