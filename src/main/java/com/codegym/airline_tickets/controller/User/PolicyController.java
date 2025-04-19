package com.codegym.airline_tickets.controller.User;

import org.apache.poi.ss.formula.atp.Switch;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/policy")
public class PolicyController {

    @GetMapping("/{id}")
    public String policy(@PathVariable ("id") int id) {

        switch (id) {
            case 1 -> {
                return "user/policy/privacy";
            }
            case 2 -> {
                return "user/policy/baggage";
            }
            case 3 -> {
                return "user/policy/book_ticket";
            }
            case 4 -> {
                return "user/policy/ticket_cancellation";
            }
            case 5 -> {
                return "user/policy/change_ticket";
            }
            case 6 -> {
                return "user/policy/protection";
            }
            case 7 -> {
                return "user/policy/specialService";
            }
            case 8 -> {
                return "user/policy/payment";
            }
        }
        return "user/homepage/homepage";

    }
}
