package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.BookingDTO;
import com.codegym.airline_tickets.dto.BookingTicketDTO;
import com.codegym.airline_tickets.entity.*;
import com.codegym.airline_tickets.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/booking")
public class BookingRestController {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IFlightService flightService;

    @Autowired
    private IFlightSeatService flightSeatService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITicketService ticketService;

    private LocalDateTime createdAt = LocalDateTime.now();

    private static final Integer PRICE_FOR_A_KG = 10000;

    @PostMapping()
    public ResponseEntity<?> create(@Validated @ModelAttribute("data") BookingDTO data, HttpSession session,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Trả về JSON đúng format
            Map<String, Object> errors = new HashMap<>();
            errors.put("errors", true);
            errors.put("message", "Validation failed");
            Map<String, String> validations = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                validations.put(error.getField(), error.getDefaultMessage());
            });
            errors.put("validator", validations);

            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON) // Chắc chắn trả về JSON
                    .body(errors);
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.getAccountByEmail(email);
        Map<String, String> dataConfirm = (Map<String, String>) session.getAttribute("confirm-data" + data.getKey());
        int num_of_adult = Integer.parseInt(dataConfirm.get("num_of_adult"));
        int num_of_child = Integer.parseInt(dataConfirm.get("num_of_child"));
        int num_of_seat = num_of_adult + num_of_child;
        Booking booking = new Booking(
                data.getId(),
                account.getUser(),
                data.getFlight(),
                data.getReturnFlight(),
                data.getFlightType(),
                data.getNumberOfTickets(),
                1,
                data.getTotalPrice(),
                createdAt
        );
        Booking res = bookingService.updateOrCreate(booking);
        List<FlightSeat> seats = flightSeatService.allocateSeats(data.getFlight().getId(), num_of_seat);
        saveTicket(data, seats, res, res.getFlight(), 1);
        List<FlightSeat> seatReturn = new ArrayList<>();
        if (res.getReturnFlight() != null) {
            seatReturn = flightSeatService.allocateSeats(data.getReturnFlight().getId(), num_of_seat);
            saveTicket(data, seatReturn, res, res.getReturnFlight(), 2);
        }
        Map<String, String> result = new HashMap<>();
        result.put("errors", "true");
        result.put("message", "Vui lòng thực hiện thanh toán");

        return ResponseEntity.ok(result);
    }

    private void saveTicket(BookingDTO data, List<FlightSeat> seats, Booking res, Flight flight, Integer flightType) {
        List<BookingTicketDTO> items = data.getItems();
        int extraKg = 0;
        BigInteger price = new BigInteger("0");
        int i_seat = 0;
        for (int i = 0; i < items.size(); i++) {
            BookingTicketDTO item = items.get(i);
            FlightSeat s;
            if (item.getCustomerType() == 3) {
                s = seats.get(i_seat);
                price = BigInteger.valueOf(100000);
                i_seat++;
            } else {
                s = seats.get(i);
                price = flight.getPrice();
            }
            if (item.getCustomerType() == 1) {
                extraKg = (flightType == 2 ? item.getExtraKgReturn() : item.getExtraKgGo());
            }
            Ticket ticket = new Ticket(
                    item.getId(),
                    res,
                    flight,
                    s,
                    item.getFullName(),
                    item.getGender(),
                    item.getBirthDate(),
                    item.getPhone(),
                    item.getCitizenIdentification(),
                    item.getEmail(),
                    extraKg,
                    price.add(BigInteger.valueOf(extraKg * PRICE_FOR_A_KG)),
                    item.getCustomerType(),
                    item.getNationality()
            );
            Ticket result = ticketService.updateOrCreate(ticket);
            FlightSeat seat = new FlightSeat(
                    s.getId(),
                    2
            );
            flightSeatService.save(seat);
        }
    }
}
