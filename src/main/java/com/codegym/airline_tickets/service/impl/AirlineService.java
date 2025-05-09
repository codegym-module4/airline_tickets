package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.repository.AirlineRepository;
import com.codegym.airline_tickets.service.IAccountService;
import com.codegym.airline_tickets.service.IAirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineService implements IAirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Override
    public List<Airline> getAll() {
        return airlineRepository.findAll();
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
        return airlineRepository.findById(id).orElse(null);
    }

    @Override
    public List<Airline> findByName(String name) {
        return List.of();
    }
}