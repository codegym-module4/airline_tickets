package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.FlightRequestDTO;
import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.service.impl.AirportService;
import com.codegym.airline_tickets.service.impl.FlightService;
import com.codegym.airline_tickets.util.FormaterCustom;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/user")
@Slf4j(topic = "FLIGHT-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class FlightsController {

    private final FlightService flightService;
    private final AirportService airportService;

    @PostMapping("/select-flight")
    public String getTopCheapestTicket(@ModelAttribute("flightReq") FlightRequestDTO flightReq, BindingResult bindingResult,
                                       @RequestParam(required = false, defaultValue = "ASC") String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model,
                                       HttpServletResponse response
    ){
        log.info("Get flight list");

        if(flightReq.getSortProperty() == null){
            return "redirect:/user/homepage";
        }

       if(flightReq != null && flightReq.getType().equals("ROUND-TRIP")) {
           String departure = flightReq.getDepartureAirport();
           String arrival = flightReq.getArrivalAirport().replaceAll("^,|,$", "");

           LocalDate departureTime = LocalDate.from(flightReq.getDepartureTime());
           LocalDate arrivalTime = LocalDate.from(flightReq.getArrivalTime());



           List<FlightResponseDTO> listDeparture = flightService.findAll(departure, arrival, departureTime, flightReq.getSortProperty(), sort, page, size);
           List<FlightResponseDTO> listArrival = flightService.findAll(arrival, departure,  arrivalTime, flightReq.getSortProperty(), sort, page, size);
           List<Airport> listAirports = airportService.getAll();

           if(listDeparture.isEmpty() || listArrival.isEmpty()){
               return "redirect:/user/homepage";
           }
           model.addAttribute("flightReq",flightReq);
           model.addAttribute("listDeparture",listDeparture);
           model.addAttribute("listArrival",listArrival);
           model.addAttribute("listAirports",listAirports);


           model.addAttribute("departure", departure);
           model.addAttribute("arrival", arrival);
           model.addAttribute("departureTime", FormaterCustom.formatDateResponse(departureTime) );
           model.addAttribute("arrivalTime",FormaterCustom.formatDateResponse(arrivalTime));

           model.addAttribute("dayOfWeekDeparture",FormaterCustom.formatDayOfWeek(departureTime));
           model.addAttribute("dayOfWeekArrival",FormaterCustom.formatDayOfWeek(arrivalTime));
       }

       if(flightReq != null && flightReq.getType().equals("ONEWAY")) {

            String departure = flightReq.getDepartureAirport();
            String arrival = flightReq.getArrivalAirport().replaceAll("^,|,$", "");

            LocalDate departureTime = LocalDate.from(flightReq.getDepartureTime());

            List<FlightResponseDTO> listDeparture = flightService.findAll(departure, arrival, departureTime,flightReq.getSortProperty(), sort, page, size);
            List<Airport> listAirports = airportService.getAll();

            if(listDeparture.isEmpty()){
                return "redirect:/user/homepage";
            }
           model.addAttribute("flightReq",flightReq);
            model.addAttribute("listDeparture",listDeparture);
            model.addAttribute("listAirports",listAirports);

            model.addAttribute("departure", departure);
            model.addAttribute("arrival", arrival);
            model.addAttribute("departureTime", FormaterCustom.formatDateResponse(departureTime));
           model.addAttribute("dayOfWeekDeparture",FormaterCustom.formatDayOfWeek(departureTime));
        }

        return "user/flight/flight";

    }

}
