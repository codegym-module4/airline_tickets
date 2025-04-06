package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.FlightRequestDTO;
import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.service.impl.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/user")
@Slf4j(topic = "FLIGHT-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class FlightsController {

    private final FlightService flightService;

    @GetMapping("/flights/cheapest")
    public String getTopCheapestTicket(@RequestParam(required = false) String departure,
                                       @RequestParam(required = false) String arrival,
                                       @RequestParam(required = false) LocalDate departureTime,
                                       @RequestParam(required = false) LocalDate arrivalTime,
                                       @RequestParam(required = false, defaultValue = "ASC") String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model
                                       ){
        log.info("Get flight list");

        List<FlightResponseDTO> listFlights = flightService.findAll(departure, arrival, departureTime, arrivalTime, sort, page, size);
//        System.out.println(listFlights);
        if(listFlights.isEmpty()){
            return "redirect:/user/homepage";
        }

        model.addAttribute("departure", departure);
        model.addAttribute("arrival", arrival);
        model.addAttribute("departureTime", departureTime);
        model.addAttribute("arrivalTime", arrivalTime);
        model.addAttribute("listFlights",listFlights);
        return "user/view_flight/view_flight";
    }

    @PostMapping("/flights/cheapest")
    public String getTopCheapestTicket(@ModelAttribute("flightReq") FlightRequestDTO flightReq, BindingResult bindingResult,
                                       @RequestParam(required = false, defaultValue = "ASC") String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model
    ){
        log.info("Get flight list");
        System.out.println(flightReq);
       if(flightReq != null){
           String departure = flightReq.getDepartureAirport();
           String arrival = flightReq.getArrivalAirport();
           LocalDate departureTime = LocalDate.from(flightReq.getDepartureTime());
           LocalDate arrivalTime = LocalDate.from(flightReq.getArrivalTime());
             List<FlightResponseDTO> listFlights = flightService.findAll(departure, arrival, departureTime, arrivalTime, sort, page, size);
             System.out.println(listFlights);
           if(listFlights.isEmpty()){
               return "redirect:/user/homepage";
           }
           model.addAttribute("listFlights",listFlights);
           model.addAttribute("departure", flightReq.getDepartureAirport());
           model.addAttribute("arrival", flightReq.getArrivalAirport());
           model.addAttribute("departureTime", flightReq.getDepartureTime());
           model.addAttribute("arrivalTime", flightReq.getArrivalTime());

       }
        return "user/view_flight/view_flight";
    }

//    @GetMapping("/test")
//    public String test(){
//        return "user/view_ticket/view_ticket";
//    }
}
