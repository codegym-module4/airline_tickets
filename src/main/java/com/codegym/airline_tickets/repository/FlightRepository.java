package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {

}
