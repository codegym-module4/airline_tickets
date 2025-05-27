package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Flight;

import java.time.LocalDate;
import java.util.List;

public interface IFlightService extends IService<Flight> {
    List<FlightResponseDTO> findAll(String departure, String arrival, LocalDate departureTime, String sortProperty, String sort, int page, int size);

    List<Flight> findByIdCompare(List<Long> ids);
}
