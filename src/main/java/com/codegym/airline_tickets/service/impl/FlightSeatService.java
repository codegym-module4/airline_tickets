package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.FlightSeatDTO;
import com.codegym.airline_tickets.entity.FlightSeat;
import com.codegym.airline_tickets.repository.FlightSeatRepository;
import com.codegym.airline_tickets.response.SeatAvailable;
import com.codegym.airline_tickets.service.IFlightSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightSeatService implements IFlightSeatService {

    @Autowired
    private FlightSeatRepository flightSeatRepository;


    @Override
    public List<FlightSeat> getAll() {
        return List.of();
    }

    @Override
    public void save(FlightSeat s) {
        flightSeatRepository.save(s);
    }

    @Override
    public void update(long id, FlightSeat s) {

    }

    @Override
    public void remove(Long id) {
        flightSeatRepository.deleteById(id);
    }

    @Override
    public FlightSeat findById(long id) {
        return flightSeatRepository.findById(id).orElse(null);
    }

    @Override
    public List<FlightSeat> findByName(String name) {
        return List.of();
    }

    @Override
    public List<FlightSeat> findSeatsByFlightAndStatus(long flightId, int status) {
        return flightSeatRepository.findAvailableSeatsByFlight(flightId, status);
    }

    @Override
    public FlightSeat updateOrCreate(FlightSeat flightSeat) {
        return flightSeatRepository.save(flightSeat);
    }

    @Override
    public void updateStatusById(Long id, Integer status) {
        flightSeatRepository.updateSeatStatus(id, status);
    }

    @Override
    public  List<Object[]> countSeatAvailable(List<Long> flightIds) {
        return flightSeatRepository.countSeatAvailable(flightIds);
    }

    @Override
    public Integer countSingleFlightSeat(Long flightId) {
        return flightSeatRepository.countSingleFlightSeat(flightId);
    }

    @Override
    public List<FlightSeatDTO> getAllSeatByFlightId(Long flightId) {
        return flightSeatRepository.getAllSeatByFlightId(flightId);
    }

    @Override
    public FlightSeat getRandomAvailableSeat(List<Long> ids, Long flightId) {
        return flightSeatRepository.getRandomAvailableSeat(ids, flightId);
    }
}