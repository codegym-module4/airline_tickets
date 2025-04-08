package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.repository.AirportRepository;
import com.codegym.airline_tickets.service.IAirlineService;
import com.codegym.airline_tickets.service.IAirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService implements IAirportService {

//    @Override
//    public List<Airport> getAll() {
//        return List.of();
//    }

    @Autowired
    AirportRepository airportRepository;

    @Override
    public List<Airport> getAll() {
        return airportRepository.findAll();
    }

    @Override
    public void save(Airport s) {

    }

    @Override
    public void update(long id, Airport s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Airport findById(long id) {
        return null;
    }

    @Override
    public List<Airport> findByName(String name) {
        return List.of();
    }
}