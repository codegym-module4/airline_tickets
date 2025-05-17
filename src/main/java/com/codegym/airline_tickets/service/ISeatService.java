package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.dto.SeatDTO;
import com.codegym.airline_tickets.entity.Seat;

import java.util.List;

public interface ISeatService extends IService<Seat> {
    List<SeatDTO> findAllSeats();
}
