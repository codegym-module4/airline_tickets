package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.FlightSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {

    @Query("SELECT fl from FlightSeat fl where fl.flight.id = :flightId and fl.status = :status")
    List<FlightSeat> findAvailableSeatsByFlight(@Param("flightId") Long flightId, @Param("status") Integer status);
}
