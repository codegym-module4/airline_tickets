package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IFlightService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService implements IFlightService {

    @Override
    public List<Flight> getAll() {
        return List.of();
    }

    @Override
    public void save(Flight s) {

    }

    @Override
    public void update(long id, Flight s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Flight findById(long id) {
        return null;
    }

    @Override
    public List<Flight> findByName(String name) {
        return List.of();
    }
}