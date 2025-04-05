package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.FlightDTO;
import com.codegym.airline_tickets.service.impl.FlightService;
import com.codegym.airline_tickets.util.FormaterDateTime;
import com.codegym.airline_tickets.util.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Slf4j(topic = "FLIGHT-CONTROLLER")
@Validated
public class FlightsController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights/cheapest")
    public String getTopCheapestTicket(@RequestParam(required = false) String departure,
                                       @RequestParam(required = false) String arrival,
                                       @RequestParam(required = false) LocalDate departureTime,
                                       @RequestParam(required = false) LocalDate arrivalTime,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model
                                       ){
        log.info("Get flight list");
//        LocalDateTime departureTime = FormaterDateTime.format(departureTimeParam);
//        LocalDateTime arrivalTime = FormaterDateTime.format(arrivalTimeParam);

        List<FlightDTO> listFlights = flightService.findAll(departure, arrival, departureTime, arrivalTime, sort, page, size);
        System.out.println(listFlights);

//        if(bindingResult.hasErrors()) {
//            Map<String, String> listErrorsMes = ValidationMessage.getErrorMes(bindingResult);
//            redirectAttributes.addFlashAttribute("listErrorMes", listErrorsMes);
//            return "redirect:/user/homepage";
//        }
        model.addAttribute("listFlights",listFlights);
        return "user/view_flight/view_flight";
    }


//    @GetMapping("/test")
//    public String test(){
//        return "user/view_ticket/view_ticket";
//    }
}
