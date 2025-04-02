package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.entity.Seat;
import com.codegym.airline_tickets.service.IRoleService;
import com.codegym.airline_tickets.service.ISeatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService implements ISeatService {

    @Override
    public List<Seat> getAll() {
        return List.of();
    }

    @Override
    public void save(Seat s) {

    }

    @Override
    public void update(long id, Seat s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Seat findById(long id) {
        return null;
    }

    @Override
    public List<Seat> findByName(String name) {
        return List.of();
    }
}