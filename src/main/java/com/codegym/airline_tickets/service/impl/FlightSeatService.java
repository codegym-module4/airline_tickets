package com.codegym.airline_tickets.service.impl;

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

    private static final Map<String, Integer> COL_MAPPING = Map.of(
            "A", 1,
            "B", 2,
            "C", 3,
            "D", 4,
            "E", 5,
            "F", 6
    );

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

    }

    @Override
    public FlightSeat findById(long id) {
        return null;
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
    public List<FlightSeat> allocateSeats(Long flightId, int numberOfPeople) {
        List<FlightSeat> availableFlightSeats = flightSeatRepository.findAvailableSeatsByFlight(flightId, 1);

        // Group by row
        Map<String, List<FlightSeat>> seatsByRow = availableFlightSeats.stream()
                .collect(Collectors.groupingBy(flightSeat -> flightSeat.getSeat().getRow().getNumber()));

        // 1. Tìm ghế liền kề
        for (Map.Entry<String, List<FlightSeat>> entry : seatsByRow.entrySet()) {
            List<FlightSeat> seatsInRow = entry.getValue().stream()
                    .sorted(Comparator.comparing(seat -> COL_MAPPING.get(seat.getSeat().getCol().getAlphabet())))
                    .toList();

            List<FlightSeat> consecutiveSeats = findConsecutiveFlightSeats(seatsInRow, numberOfPeople);

            if (consecutiveSeats != null) {
                return consecutiveSeats;
            }
        }

        // 2. Tìm đủ ghế trong cùng hàng
        for (Map.Entry<String, List<FlightSeat>> entry : seatsByRow.entrySet()) {
            List<FlightSeat> seatsInRow = entry.getValue();
            if (seatsInRow.size() >= numberOfPeople) {
                return seatsInRow.subList(0, numberOfPeople);
            }
        }

        // 3. Ghế gần nhất bất kỳ
        List<FlightSeat> result = new ArrayList<>();
        availableFlightSeats.sort(Comparator
                .comparing((FlightSeat fs) -> fs.getSeat().getRow().getNumber())
                .thenComparing(fs -> COL_MAPPING.get(fs.getSeat().getCol().getAlphabet()))
        );

        for (FlightSeat flightSeat : availableFlightSeats) {
            result.add(flightSeat);
            if (result.size() == numberOfPeople) {
                return result;
            }
        }

        // 4. Không đủ ghế
        return Collections.emptyList();
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

    private List<FlightSeat> findConsecutiveFlightSeats(List<FlightSeat> seats, int numberOfPeople) {
        for (int i = 0; i <= seats.size() - numberOfPeople; i++) {
            boolean consecutive = true;
            for (int j = 0; j < numberOfPeople - 1; j++) {
                int col1 = COL_MAPPING.get(seats.get(i + j).getSeat().getCol().getAlphabet());
                int col2 = COL_MAPPING.get(seats.get(i + j + 1).getSeat().getCol().getAlphabet());
                if (col2 - col1 != 1) {
                    consecutive = false;
                    break;
                }
            }
            if (consecutive) {
                return seats.subList(i, i + numberOfPeople);
            }
        }
        return null;
    }
}