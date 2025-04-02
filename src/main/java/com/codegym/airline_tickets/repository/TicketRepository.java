package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Seat;
import com.codegym.airline_tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
