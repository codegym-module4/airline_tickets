package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.dto.FlightSeatDTO;
import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.entity.FlightSeat;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IFlightSeatService extends IService<FlightSeat> {
    List<FlightSeat> findSeatsByFlightAndStatus(long flightId, int status);

    List<FlightSeat> allocateSeats(Long flightId, int numberOfPeople);
}
