package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.BookingDTO;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IFlightSeatService;
import com.codegym.airline_tickets.service.IFlightService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
public class BookingRestController {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IFlightService flightService;

    @Autowired
    private IFlightSeatService flightSeatService;

    @PostMapping()
    public ResponseEntity<?> create(@Validated @ModelAttribute("data") BookingDTO data, BindingResult bindingResult) {
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
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);


        return ResponseEntity.ok(res);
    }
}
