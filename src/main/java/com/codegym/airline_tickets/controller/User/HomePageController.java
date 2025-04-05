package com.codegym.airline_tickets.controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class HomePageController {
    @GetMapping("/homepage")
    public String homepage() {
        return "user/homepage/homepage";
    }
}
