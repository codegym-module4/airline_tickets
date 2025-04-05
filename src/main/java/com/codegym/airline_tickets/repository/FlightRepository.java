package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {


    @Query(value = "SELECT flight from Flight flight  " +
            "JOIN Airport departure ON flight.departureAirport.id = departure.id " +
            "JOIN Airport arrival  ON flight.departureAirport.id = arrival.id " +
            "JOIN Airline airline ON flight.airline.id = airline.id " +
            "WHERE flight.departureAirport.name = :departure " +
            "AND flight.arrivalAirport.name = :arrival " +
            "AND DATE(flight.departure_time) = :departureTime " +
            "AND DATE(flight.arrival_time) = :arrivalTime")
    Page<Flight> searchByKeyword(@Param("departure") String departure,
                                 @Param("arrival") String arrival,
                                 @Param("departureTime") LocalDate departureTime,
                                 @Param("arrivalTime") LocalDate arrivalTime,
                                 Pageable pageable);
}
