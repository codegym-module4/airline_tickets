package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.service.impl.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping()
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights(String departure, String arrival, LocalDate departureTime, String sortProperty, String sort, int page, int size) {
        List<FlightResponseDTO> list = flightService.findAll(departure,arrival,departureTime,sortProperty,sort,page,size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
