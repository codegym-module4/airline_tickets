package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.service.IAccountService;
import com.codegym.airline_tickets.service.IAirlineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineService implements IAirlineService {

    @Override
    public List<Airline> getAll() {
        return List.of();
    }

    @Override
    public void save(Airline s) {

    }

    @Override
    public void update(long id, Airline s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Airline findById(long id) {
        return null;
    }

    @Override
    public List<Airline> findByName(String name) {
        return List.of();
    }
}