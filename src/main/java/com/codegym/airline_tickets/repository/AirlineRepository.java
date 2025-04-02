package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

}
