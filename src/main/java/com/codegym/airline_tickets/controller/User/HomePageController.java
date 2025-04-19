package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.FlightRequestDTO;
import com.codegym.airline_tickets.entity.Airport;
import com.codegym.airline_tickets.entity.News;
import com.codegym.airline_tickets.service.impl.AirportService;
import com.codegym.airline_tickets.service.impl.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j(topic = "HOMEPAGE-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class HomePageController {

    private final NewsService newsService;
    private final AirportService airportService;

    @GetMapping({"/"})
    public String homepage(Model model) {
        List<Airport> listAirports = airportService.getAll();
        FlightRequestDTO flightRequestDTO = new FlightRequestDTO();
        model.addAttribute("listAirports", listAirports);
        model.addAttribute("flightReq", flightRequestDTO);

        List<News> news = newsService.getAll();
        List<List<News>> newsList = new ArrayList<>();
        for (int i = 0; i < news.size(); i += 3) {
            newsList.add(news.subList(i, Math.min(i + 3, news.size())));
        }

        model.addAttribute("newsList", newsList);
        return "user/homepage/homepage";
    }
}