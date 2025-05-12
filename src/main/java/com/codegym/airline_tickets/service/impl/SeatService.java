package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.SeatDTO;
import com.codegym.airline_tickets.entity.Seat;
import com.codegym.airline_tickets.repository.SeatRepository;
import com.codegym.airline_tickets.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService implements ISeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<Seat> getAll() {
        return seatRepository.findAll();
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
        return seatRepository.findById(id).orElse(null);
    }

    @Override
    public List<Seat> findByName(String name) {
        return List.of();
    }


    @Override
    public List<SeatDTO> findAllSeats() {
        return seatRepository.findAllSeats();
    }
}