package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.BookingDTO;
import com.codegym.airline_tickets.dto.BookingTicketDTO;
import com.codegym.airline_tickets.dto.CountryDTO;
import com.codegym.airline_tickets.util.GetCountries;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("data_flight")
public class BookingController {

    @GetMapping("/booking")
    public String index(Model model) {
        BookingDTO data = new BookingDTO();
        List<BookingTicketDTO> list = new ArrayList<BookingTicketDTO>();
        List<CountryDTO> countries = GetCountries.getCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("data", data);

        return "user/booking/index";
    }
}
