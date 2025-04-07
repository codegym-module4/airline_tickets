package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class PaymentController {

    @Autowired
    private IBookingService bookingService;

    @GetMapping("/payment")
    public String payment(Model model) {
        List<Booking> list = bookingService.findByStatus(1);
        model.addAttribute("list", list);

        return "user/payment/payment";
    }

    @GetMapping("/transfer-history")
    public String history(Model model) {
        List<Booking> list = bookingService.getAll();
        model.addAttribute("list", list);
        return "user/payment/transfer_history";
    }
}
