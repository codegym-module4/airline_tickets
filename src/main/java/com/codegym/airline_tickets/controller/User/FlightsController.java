package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.FlightRequestDTO;
import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.service.impl.AirportService;
import com.codegym.airline_tickets.service.impl.FlightService;
import com.codegym.airline_tickets.util.BuildFlightRequest;
import com.codegym.airline_tickets.util.FormaterCustom;
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

//    @PostMapping("/select-flight")
//    public String getFlightByCondition(@Validated @ModelAttribute("flightReq") FlightRequestDTO flightReq, BindingResult bindingResult,
//                                       @RequestParam(required = false, defaultValue = "ASC") String sort,
//                                       @RequestParam(defaultValue = "0") int page,
//                                       @RequestParam(defaultValue = "10") int size,
//                                       Model model, RedirectAttributes redirectAttributes
//    ){
//        log.info("Get flight list");
//
//        if(bindingResult.hasErrors()){
//            redirectAttributes.addFlashAttribute("messageError","Bạn chưa điền đầy đủ thông tin tìm chuyến bay");
//            return "redirect:/";
//        }
//
//        if(flightReq != null && flightReq.getType().equals("ROUND-TRIP")) {
//
//            if(flightReq.getArrivalTime() == null){
//                redirectAttributes.addFlashAttribute("messageError","Bạn chưa điền đầy đủ thông tin tìm chuyến bay");
//                return "redirect:/";
//            }
//
//           String departure = flightReq.getDepartureAirport();
//           String arrival = flightReq.getArrivalAirport();
//
//           LocalDate departureTime = LocalDate.from(flightReq.getDepartureTime());
//           LocalDate arrivalTime = LocalDate.from(flightReq.getArrivalTime());
//
//
//           List<FlightResponseDTO> listDeparture = flightService.findAll(departure, arrival, departureTime, flightReq.getSortProperty(), sort, page, size);
//           List<FlightResponseDTO> listArrival = flightService.findAll(arrival, departure,  arrivalTime, flightReq.getSortProperty(), sort, page, size);
//
//           if(listDeparture.isEmpty() || listArrival.isEmpty() ){
//               redirectAttributes.addFlashAttribute("messageError","Không tìm thấy thông tin chuyến bay");
//               return "redirect:/";
//           }
//           List<Airport> listAirports = airportService.getAll();
//
//           model.addAttribute("flightReq",flightReq);
//           model.addAttribute("listDeparture",listDeparture);
//           model.addAttribute("listArrival",listArrival);
//           model.addAttribute("listAirports",listAirports);
//
//
//           model.addAttribute("departure", departure);
//           model.addAttribute("arrival", arrival);
//           model.addAttribute("departureTime", FormaterCustom.formatDateResponse(departureTime) );
//           model.addAttribute("arrivalTime",FormaterCustom.formatDateResponse(arrivalTime));
//
//           model.addAttribute("dayOfWeekDeparture",FormaterCustom.formatDayOfWeek(departureTime));
//           model.addAttribute("dayOfWeekArrival",FormaterCustom.formatDayOfWeek(arrivalTime));
//           model.addAttribute("message","Tìm kiếm thành công");
//       }
//
//       if(flightReq != null && flightReq.getType().equals("ONEWAY")) {
//
//            String departure = flightReq.getDepartureAirport();
//            String arrival = flightReq.getArrivalAirportOneWay();
//
//            LocalDate departureTime = LocalDate.from(flightReq.getDepartureTime());
//
//            List<FlightResponseDTO> listDeparture = flightService.findAll(departure, arrival, departureTime,flightReq.getSortProperty(), sort, page, size);
//            if(listDeparture.isEmpty()){
//               redirectAttributes.addFlashAttribute("messageError","Không tìm thấy thông tin chuyến bay");
//               return "redirect:/";
//            }
//            List<Airport> listAirports = airportService.getAll();
//
//           model.addAttribute("flightReq",flightReq);
//           model.addAttribute("listDeparture",listDeparture);
//           model.addAttribute("listAirports",listAirports);
//
//           model.addAttribute("departure", departure);
//           model.addAttribute("arrival", arrival);
//           model.addAttribute("departureTime", FormaterCustom.formatDateResponse(departureTime));
//           model.addAttribute("dayOfWeekDeparture",FormaterCustom.formatDayOfWeek(departureTime));
//           model.addAttribute("message","Tìm kiếm thành công");
//        }
//        return "user/flight/flight";
//    }


    @GetMapping("/select-flight")
    public String getFlightByCondition(@RequestParam Map<String,String> request,
                                       @RequestParam(required = false, defaultValue = "ASC") String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false, defaultValue = "price") String sortProperty,
                                       Model model, RedirectAttributes redirectAttributes
    ){

        log.info("Get flight list");

        FlightRequestDTO flightReq = BuildFlightRequest.build(request,sortProperty);


        if(flightReq == null ){
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
            if(flightReq.getPrice() != null){
                List<FlightResponseDTO> listHotdeal = flightService.findFightHotDeal(departure, arrival, flightReq.getPrice(), sortProperty, sort, page, size);
                model.addAttribute("listDeparture",listHotdeal);
                if(listHotdeal.isEmpty()){
                    redirectAttributes.addFlashAttribute("messageError","Không tìm thấy thông tin chuyến bay");
                    return "redirect:/";
                }

            }else {
            List<FlightResponseDTO> listDeparture = flightService.findAll(departure, arrival, departureTime,flightReq.getSortProperty(), sort, page, size);
                model.addAttribute("listDeparture",listDeparture);
                if(listDeparture.isEmpty()){
                    redirectAttributes.addFlashAttribute("messageError","Không tìm thấy thông tin chuyến bay");
                    return "redirect:/";
                }
            }

            List<Airport> listAirports = airportService.getAll();

           model.addAttribute("flightReq",flightReq);
           model.addAttribute("listAirports",listAirports);
           model.addAttribute("departure", departure);
           model.addAttribute("arrival", arrival);
           model.addAttribute("departureTime", FormaterCustom.formatDateResponse(departureTime));
           model.addAttribute("dayOfWeekDeparture",FormaterCustom.formatDayOfWeek(departureTime));
           model.addAttribute("message","Tìm kiếm thành công");
        }
        return "user/flight/flight";
    }



    @GetMapping("/select-hotdeal")
    public String getFlightHotDeal (@RequestParam Map<String, String> request,
                                    @RequestParam(required = false, defaultValue = "ASC") String sort,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false, defaultValue = "price") String sortProperty,
                                    Model model, RedirectAttributes redirectAttributes){

        System.out.println(request);
        request.put("arrivalAirportOneWay","");
        request.put("departureTime","");
        request.put("arrivalTime","");

        String departure = request.get("departure");
        String arrival = request.get("arrival");
        int price = Integer.parseInt(request.get("price"));

        log.info("Get flight discount");

        List<FlightResponseDTO> listDeparture = flightService.findFightHotDeal(departure, arrival, price, sortProperty, sort, page, size);
        if(listDeparture.isEmpty()){
            redirectAttributes.addFlashAttribute("messageError","Không tìm thấy thông tin chuyến bay");
            return "redirect:/";
        }



        // to do something
        return "user/flight/flight";
    }



}
