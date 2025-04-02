package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IAirportService;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {
    @Override
    public List<Booking> getAll() {
        return List.of();
    }

    @Override
    public void save(Booking s) {

    }

    @Override
    public void update(long id, Booking s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Booking findById(long id) {
        return null;
    }

    @Override
    public List<Booking> findByName(String name) {
        return List.of();
    }
}