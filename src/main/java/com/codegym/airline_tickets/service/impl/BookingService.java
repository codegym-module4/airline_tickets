package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.repository.BookingRepository;
import com.codegym.airline_tickets.service.IAirportService;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;


    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
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

    @Override
    public List<Booking> findByStatus(int status) {
        return bookingRepository.findByStatus(status);
    }
}