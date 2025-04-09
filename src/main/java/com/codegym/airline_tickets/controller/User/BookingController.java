package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.CountryDTO;
import com.codegym.airline_tickets.util.GetCountries;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BookingController {

    @GetMapping("/booking")
    public String index(Model model) {

        List<CountryDTO> countries = GetCountries.getCountries();
        model.addAttribute("countries", countries);

        return "user/booking/index";
    }
}
