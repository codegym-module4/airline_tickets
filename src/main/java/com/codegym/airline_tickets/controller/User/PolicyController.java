package com.codegym.airline_tickets.controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/policy")
public class PolicyController {

    @GetMapping("/{name}")
    public String policy(@PathVariable ("name") String name) {

        switch (name) {
            case "chinh-sach-bao-mat-quyen-rieng-tu" -> {
                return "user/policy/privacy";
            }
            case "chinh-sach-hanh-ly" -> {
                return "user/policy/baggage";
            }
            case "chinh-sach-dat-ve" -> {
                return "user/policy/book_ticket";
            }
            case "chinh-sach-huy-ve" -> {
                return "user/policy/ticket_cancellation";
            }
            case "chinh-sach-doi-ve" -> {
                return "user/policy/change_ticket";
            }
            case "chinh-sach-bao-ve-khach-hang" -> {
                return "user/policy/protection";
            }
            case "chinh-sach-ho-tro-dac-biet" -> {
                return "user/policy/specialService";
            }
            case "kenh-thanh-toan" -> {
                return "user/policy/payment";
            }
        }

        return "redirect:/";

    }

}
