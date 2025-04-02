package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Col;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IColService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColService implements IColService {

    @Override
    public List<Col> getAll() {
        return List.of();
    }

    @Override
    public void save(Col s) {

    }

    @Override
    public void update(long id, Col s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Col findById(long id) {
        return null;
    }

    @Override
    public List<Col> findByName(String name) {
        return List.of();
    }
}