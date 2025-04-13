package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.repository.FlightRepository;
import com.codegym.airline_tickets.service.IFlightService;
import com.codegym.airline_tickets.util.FormaterCustom;
import com.codegym.airline_tickets.util.SortOrderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

        return flightRepository.findNotDeletedById(id);
    }

    @Override
    public List<Flight> findByName(String name) {
        return List.of();
    }

    @Override
    public List<FlightResponseDTO> findAll(String departure, String arrival, LocalDate departureTime, String sortProperty, String sort, int page, int size) {
        log.info("findAll flight start");

        // Sorting
//        Sort.Order order = new Sort.Order(Sort.Direction.ASC, sortProperty);
//        if (StringUtils.hasLength(sort)) {
//            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); // tencot:asc|desc
//            Matcher matcher = pattern.matcher(sort);
//            if (matcher.find()) {
//                String columnName = matcher.group(1);
//                if (matcher.group(3).equalsIgnoreCase("asc")) {
//                    order = new Sort.Order(Sort.Direction.ASC, columnName);
//                } else {
//                    order = new Sort.Order(Sort.Direction.DESC, columnName);
//                }
//            }
//        }
        Sort.Order order = SortOrderHelper.createOrder(sortProperty, sort);

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

    public static List<FlightResponseDTO> getFlightPageResponse(int page, int size, Page<Flight> flightPage){
        log.info("Convert Flight Entity Page");

        return flightPage.stream().map(flight -> FlightResponseDTO.builder()
                .id(flight.getId())
                .flightCode(flight.getCode())
                .airlineName(flight.getAirline().getName())
                .departureTime(flight.getDeparture_time())
                .arrivalTime(flight.getArrival_time())
                .price(FormaterCustom.withLargeIntegers(flight.getPrice()))
                .departureAirportCity(flight.getDepartureAirport().getCity())
                .arrivalAirportCity(flight.getArrivalAirport().getCity())
                .priceVATTotal(FormaterCustom.formatPriceVAT(flight.getPrice()))
                .priceVAT(FormaterCustom.calPriceVAT(flight.getPrice()))
                .build()
        ).toList();
    }

    public  List<FlightResponseDTO> searchFightDiscount (String departure, String arrival, int price, String sortProperty, String sort, int page, int size){

        Sort.Order order = SortOrderHelper.createOrder(sortProperty, sort);

        int pageNo = 0;
        if (page > 0) {
            pageNo = page - 1;
        }

        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));

        Page<Flight> flightPage;

        if (StringUtils.hasLength(departure) && StringUtils.hasLength(arrival)) {
            flightPage = flightRepository.searchFightDiscount(departure, arrival, price, pageable);
        }else {
            flightPage = flightRepository.findAll(pageable);
        }
        return getFlightPageDiscount(page, size, flightPage);
    }

     public static List<FlightResponseDTO> getFlightPageDiscount (int page, int size, Page<Flight> flightPage){
         return flightPage.stream().map(flight -> FlightResponseDTO.builder()
                 .id(flight.getId())
                 .flightCode(flight.getCode())
                 .airlineName(flight.getAirline().getName())
                 .departureTime(flight.getDeparture_time())
                 .arrivalTime(flight.getArrival_time())
                 .price(FormaterCustom.withLargeIntegers(flight.getPrice()))
                 .departureAirportCity(flight.getDepartureAirport().getCity())
                 .arrivalAirportCity(flight.getArrivalAirport().getCity())
                 .priceVATTotal(FormaterCustom.formatPriceVAT(flight.getPrice()))
                 .priceVAT(FormaterCustom.calPriceVAT(flight.getPrice()))
                 .build()
         ).toList();
    }

}