package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.dto.FlightSeatDTO;
import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.entity.FlightSeat;
import com.codegym.airline_tickets.response.SeatAvailable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IFlightSeatService extends IService<FlightSeat> {
    List<FlightSeat> findSeatsByFlightAndStatus(long flightId, int status);

    FlightSeat updateOrCreate(FlightSeat flightSeat);

    void updateStatusById(Long id, Integer status);

    List<Object[]> countSeatAvailable (List<Long> flightIds);

    Integer countSingleFlightSeat(Long flightId);

    List<FlightSeatDTO> getAllSeatByFlightId(Long flightId);

    FlightSeat getRandomAvailableSeat(List<Long> ids, Long flightId);
}
