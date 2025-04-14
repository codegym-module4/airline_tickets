package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.response.FlightResponse;
import com.codegym.airline_tickets.service.IFlightService;
import com.codegym.airline_tickets.service.impl.FlightService;
import com.codegym.airline_tickets.util.FormaterCustom;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @GetMapping("/confirm")
    public ResponseEntity<FlightResponse> getDataFlightConfirm(@RequestParam Map<String, String> request) {
        int num_of_adult = Integer.parseInt(request.get("num_of_adult"));
        int num_of_child = Integer.parseInt(request.get("num_of_child"));
        int num_of_ticket =  num_of_adult + num_of_child;
        int num_of_baby = Integer.parseInt(request.get("num_of_baby"));
        BigInteger total = BigInteger.valueOf(0);
        Flight flightDepart = flightService.findById(Long.parseLong(request.get("idDepart")));
        if (flightDepart == null) {
            return ResponseEntity.badRequest().body(
                    FlightResponse.builder()
                            .errors(true)
                            .status(HttpStatus.NOT_FOUND)
                            .message("Không tìm thấy thông tin chuyến bay đi")
                            .build()
            );
        }
        BigInteger totalDepart = new BigDecimal(flightDepart.getPrice()).multiply(BigDecimal.valueOf(num_of_ticket)).toBigInteger();
        total = total.add(totalDepart).add(BigInteger.valueOf(num_of_baby * 100000));
        FlightResponseDTO flightDepartDTO = FlightResponseDTO.builder()
                    .id(flightDepart.getId())
                    .flightCode(flightDepart.getCode())
                    .airlineName(flightDepart.getAirline().getName())
                    .departureTime(flightDepart.getDeparture_time())
                    .arrivalTime(flightDepart.getArrival_time())
                    .price(FormaterCustom.withLargeIntegers(flightDepart.getPrice()))
                    .departureAirportCity(flightDepart.getDepartureAirport().getCity())
                    .arrivalAirportCity(flightDepart.getArrivalAirport().getCity())
                    .departureAirportName(flightDepart.getDepartureAirport().getName())
                    .arrivalAirportName(flightDepart.getArrivalAirport().getName())
                    .priceVATTotal(FormaterCustom.withLargeIntegers(totalDepart))
                    .priceVAT("0")
                    .build();
        FlightResponseDTO flightArrivalDTO = new FlightResponseDTO();
        if (request.get("idArrival") != null && !request.get("idArrival").isEmpty()) {
            Flight flightArrival = flightService.findById(Long.parseLong(request.get("idArrival")));
            if (flightArrival == null) {
                return ResponseEntity.badRequest().body(
                        FlightResponse.builder()
                                .errors(true)
                                .status(HttpStatus.NOT_FOUND)
                                .message("Không tìm thấy thông tin chuyến bay đi")
                                .build()
                );
            }
            BigInteger totalArrival = new BigDecimal(flightArrival.getPrice()).multiply(BigDecimal.valueOf(num_of_ticket)).toBigInteger();
            total = total.add(totalArrival).add(BigInteger.valueOf(num_of_baby * 100000));
            flightArrivalDTO = FlightResponseDTO.builder()
                    .id(flightArrival.getId())
                    .flightCode(flightArrival.getCode())
                    .airlineName(flightArrival.getAirline().getName())
                    .departureTime(flightArrival.getDeparture_time())
                    .arrivalTime(flightArrival.getArrival_time())
                    .price(FormaterCustom.withLargeIntegers(flightArrival.getPrice()))
                    .departureAirportCity(flightArrival.getDepartureAirport().getCity())
                    .arrivalAirportCity(flightArrival.getArrivalAirport().getCity())
                    .departureAirportName(flightArrival.getDepartureAirport().getName())
                    .arrivalAirportName(flightArrival.getArrivalAirport().getName())
                    .priceVATTotal(FormaterCustom.withLargeIntegers(totalArrival))
                    .priceVAT("0")
                    .build();
        }
        Context context = new Context();
        context.setVariable("num_of_ticket", num_of_ticket);
        context.setVariable("num_of_baby", num_of_baby);
        context.setVariable("flightDepart", flightDepartDTO);
        context.setVariable("flightArrival", flightArrivalDTO);
        context.setVariable("total", total);
        String html = templateEngine.process("user/flight/data_flight_confirm", context);

        return ResponseEntity.ok().body(
                FlightResponse.builder()
                        .status(HttpStatus.OK)
                        .html(html)
                        .build()
        );
    }

    @PostMapping("/accept-booking")
    public ResponseEntity<?> acceptBooking(@RequestParam Map<String, String> request, HttpSession session) {
        String key = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute("confirm-data" + key, request);

        Map<String, String> res = new HashMap<>();
        res.put("errors", "false");
        res.put("url", "/booking/" + key);

        return ResponseEntity.ok(res);
    }

}
