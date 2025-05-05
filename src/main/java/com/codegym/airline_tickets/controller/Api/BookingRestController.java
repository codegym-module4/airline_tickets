package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.BookingDTO;
import com.codegym.airline_tickets.dto.BookingTicketDTO;
import com.codegym.airline_tickets.entity.*;
import com.codegym.airline_tickets.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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

    @PostMapping()
    @Transactional
    public ResponseEntity<?> create(@ModelAttribute("data") @Validated BookingDTO data, BindingResult bindingResult, HttpSession session) {
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
        session.setAttribute("BookingDTO" + data.getKey(), data);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Vui lòng chọn ghế (Không bắt buộc)");
        result.put("url", "/booking/" + data.getKey() + "/seat-selection/" + data.getFlight().getId());

        return ResponseEntity.ok(result);
    }

}
