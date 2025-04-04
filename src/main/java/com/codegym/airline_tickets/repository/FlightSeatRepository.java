package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.FlightSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {

}
