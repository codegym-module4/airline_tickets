package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.service.impl.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightRestController {

    private final FlightService flightService;

    @GetMapping()
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
//        List<FlightResponseDTO> medicines = flightService.findAll();
        List<FlightResponseDTO> medicines = new ArrayList<FlightResponseDTO>();
        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }
}
