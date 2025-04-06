package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.repository.FlightRepository;
import com.codegym.airline_tickets.service.IFlightService;
import com.codegym.airline_tickets.util.FormaterCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "FLIGHT-SERVICE")
public class FlightService implements IFlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public List<Flight> getAll() {
        return List.of();
    }

    @Override
    public void save(Flight s) {
//
    }

    @Override
    public void update(long id, Flight s) {
    //
    }

    @Override
    public void remove(Long id) {
        //
    }

    @Override
    public Flight findById(long id) {
        return null;
    }

    @Override
    public List<Flight> findByName(String name) {
        return List.of();
    }


    public List<FlightResponseDTO> findAll(String departure, String arrival, LocalDate departureTime, String sort, int page, int size) {
        log.info("findAll flight start");

        // Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "airline");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); // tencot:asc|desc
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }

        // handle start page = 1
        int pageNo = 0;
        if (page > 0) {
            pageNo = page - 1;
        }

        // Paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));

        Page<Flight> flightPage;

        if (StringUtils.hasLength(departure) && StringUtils.hasLength(arrival) ) {
            flightPage = flightRepository.searchByKeyword(departure, arrival, departureTime, pageable);
        } else {
            flightPage = flightRepository.findAll(pageable);
        }

        return getFlightPageResponse(page, size, flightPage);

    }

    private static List<FlightResponseDTO> getFlightPageResponse(int page, int size, Page<Flight> flightPage){
        log.info("Convert Flight Entity Page");
        System.out.println(flightPage);
        return flightPage.stream().map(flight -> FlightResponseDTO.builder()
                .flightCode(flight.getCode())
                .airlineName(flight.getAirline().getName())
                .departureTime(flight.getDeparture_time())
                .arrivalTime(flight.getArrival_time())
                .price(FormaterCustom.withLargeIntegers(flight.getPrice()))
                .build()
        ).toList();
    }

}