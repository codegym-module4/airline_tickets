package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.FlightSeat;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IFlightSeatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LightSeatService implements IFlightSeatService {

    @Override
    public List<FlightSeat> getAll() {
        return List.of();
    }

    @Override
    public void save(FlightSeat s) {

    }

    @Override
    public void update(long id, FlightSeat s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public FlightSeat findById(long id) {
        return null;
    }

    @Override
    public List<FlightSeat> findByName(String name) {
        return List.of();
    }
}