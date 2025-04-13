package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.FlightRequestDTO;
import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.service.impl.AirportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j(topic = "HOMEPAGE-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class HomePageController {

    private final AirportService airportService;

    @GetMapping({"/"})
    public String homepage(Model model) {
        List<Airport> listAirports = airportService.getAll();
        FlightRequestDTO flightRequestDTO = new FlightRequestDTO();
        model.addAttribute("listAirports", listAirports);
        model.addAttribute("flightReq", flightRequestDTO);
        return "user/homepage/homepage";
    }
}