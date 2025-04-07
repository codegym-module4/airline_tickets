package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IBookingService bookingService;

    @GetMapping()
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

    @PostMapping("/payment-callback")
    public String paymentCallback(@RequestParam Map<String, String> params, Model model) {
        String status = params.get("vnp_ResponseCode");
        if (Objects.equals(status, "00")) {
            return "redirect:/payment/success";
        }

        return "redirect:/payment/fail";
    }

    @GetMapping("/payment/success")
    public String success(Model model) {

        return "user/payment/success";
    }

    @GetMapping("/payment/fail")
    public String fail(Model model) {

        return "user/payment/fail";
    }
}
