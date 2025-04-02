package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {

}
