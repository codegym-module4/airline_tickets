package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.FlightRequestDTO;
import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.service.impl.AirportService;
import com.codegym.airline_tickets.service.impl.FlightService;
import com.codegym.airline_tickets.util.FormaterCustom;
import com.codegym.airline_tickets.util.ValidationMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/user")
@Slf4j(topic = "FLIGHT-CONTROLLER")
@RequiredArgsConstructor

public class FlightsController {

    private final FlightService flightService;
    private final AirportService airportService;

    @PostMapping("/select-flight")
    public String getTopCheapestTicket(@Validated @ModelAttribute("flightReq") FlightRequestDTO flightReq, BindingResult bindingResult,
                                       @RequestParam(required = false, defaultValue = "ASC") String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model, RedirectAttributes redirectAttributes
    ){
        log.info("Get flight list");

        if(bindingResult.hasErrors()){

            redirectAttributes.addFlashAttribute("messageError","Bạn chưa điền đầy đủ thông tin tìm chuyến bay");
            return "redirect:/";
        }



        if(flightReq != null && flightReq.getType().equals("ROUND-TRIP")) {

            if(flightReq.getArrivalTime() == null){
                redirectAttributes.addFlashAttribute("messageError","Bạn chưa điền đầy đủ thông tin tìm chuyến bay");
                return "redirect:/";
            }

           String departure = flightReq.getDepartureAirport();
           String arrival = flightReq.getArrivalAirport();

           LocalDate departureTime = LocalDate.from(flightReq.getDepartureTime());
           LocalDate arrivalTime = LocalDate.from(flightReq.getArrivalTime());


           List<FlightResponseDTO> listDeparture = flightService.findAll(departure, arrival, departureTime, flightReq.getSortProperty(), sort, page, size);
           List<FlightResponseDTO> listArrival = flightService.findAll(arrival, departure,  arrivalTime, flightReq.getSortProperty(), sort, page, size);

           if(listDeparture.isEmpty() || listArrival.isEmpty() ){
               redirectAttributes.addFlashAttribute("messageError","Không tìm thấy thông tin chuyến bay");
               return "redirect:/";
           }
           List<Airport> listAirports = airportService.getAll();

           if(listDeparture.isEmpty() || listArrival.isEmpty()){
               return "redirect:/";
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
           model.addAttribute("message","Tìm kiếm thành công");
       }

       if(flightReq != null && flightReq.getType().equals("ONEWAY")) {

            String departure = flightReq.getDepartureAirport();
            String arrival = flightReq.getArrivalAirportOneWay();

            LocalDate departureTime = LocalDate.from(flightReq.getDepartureTime());

            List<FlightResponseDTO> listDeparture = flightService.findAll(departure, arrival, departureTime,flightReq.getSortProperty(), sort, page, size);
            if(listDeparture.isEmpty()){
               redirectAttributes.addFlashAttribute("messageError","Không tìm thấy thông tin chuyến bay");
               return "redirect:/";
            }
            List<Airport> listAirports = airportService.getAll();

            if(listDeparture.isEmpty()){
                return "redirect:/";
            }
           model.addAttribute("flightReq",flightReq);
            model.addAttribute("listDeparture",listDeparture);
            model.addAttribute("listAirports",listAirports);

            model.addAttribute("departure", departure);
            model.addAttribute("arrival", arrival);
            model.addAttribute("departureTime", FormaterCustom.formatDateResponse(departureTime));
           model.addAttribute("dayOfWeekDeparture",FormaterCustom.formatDayOfWeek(departureTime));
           model.addAttribute("message","Tìm kiếm thành công");
        }

        return "user/flight/flight";

    }

}
