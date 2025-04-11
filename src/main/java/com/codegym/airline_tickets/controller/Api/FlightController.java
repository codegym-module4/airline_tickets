package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.response.FlightResponse;
import com.codegym.airline_tickets.service.IFlightService;
import com.codegym.airline_tickets.service.impl.FlightService;
import com.codegym.airline_tickets.util.FormaterCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    @Autowired
    private final IFlightService flightService;

    @Autowired
    private TemplateEngine templateEngine;

    @PostMapping()
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights(String departure, String arrival, LocalDate departureTime, String sortProperty, String sort, int page, int size) {
        List<FlightResponseDTO> list = flightService.findAll(departure,arrival,departureTime,sortProperty,sort,page,size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getDataFlightById(@PathVariable("id") Long id) {
        Flight flight = flightService.findById(id);
        if (flight == null) {
            return ResponseEntity.badRequest().body(
                    FlightResponse.builder()
                            .errors(true)
                            .status(HttpStatus.NOT_FOUND)
                            .message("Không tìm thấy thông tin chuyến bay")
                            .build()
            );
        }

        String departure = flight.getDepartureAirport().getName();
        String arrival = flight.getArrivalAirport().getName();
        FlightResponseDTO flightResponseDTO = FlightResponseDTO.builder()
                .id(flight.getId())
                .flightCode(flight.getCode())
                .airlineName(flight.getAirline().getName())
                .departureTime(flight.getDeparture_time())
                .arrivalTime(flight.getArrival_time())
                .price(FormaterCustom.withLargeIntegers(flight.getPrice()))
                .departureAirportCity(flight.getDepartureAirport().getCity())
                .arrivalAirportCity(flight.getArrivalAirport().getCity())
                .priceVATTotal(FormaterCustom.formatPriceVAT(flight.getPrice()))
                .priceVAT("0")
                .build();
        Context context = new Context();
        context.setVariable("departure", departure);
        context.setVariable("arrival", arrival);
        context.setVariable("flight", flightResponseDTO);
        String html = templateEngine.process("user/flight/data_flight", context);

        return ResponseEntity.ok().body(
                FlightResponse.builder()
                        .status(HttpStatus.OK)
                        .html(html)
                        .build()
        );
    }

}
