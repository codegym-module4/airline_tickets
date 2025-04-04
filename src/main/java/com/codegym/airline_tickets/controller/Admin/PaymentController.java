package com.codegym.airline_tickets.controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class PaymentController {

    @GetMapping("/payment")
    public String payment() {
        return "user/payment/payment";
    }

    @GetMapping("/tranfer-history")
    public String history() {
        return "user/payment/tranfer_history";
    }
}
