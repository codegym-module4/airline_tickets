package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Row;
import com.codegym.airline_tickets.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

}
