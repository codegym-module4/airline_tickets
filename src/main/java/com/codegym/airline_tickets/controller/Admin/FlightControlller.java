package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.service.impl.AirlineService;
import com.codegym.airline_tickets.service.impl.AirportService;
import com.codegym.airline_tickets.service.impl.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/flights")
public class FlightControlller {
    @Autowired
    private FlightService flightService;

    @Autowired
    private AirlineService airlineService;

    @Autowired
    private AirportService airportService;

    @GetMapping()
    public String listFlight(Model model) {
        model.addAttribute("flightList", flightService.getAll());
        return "admin/flights/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("airlines", airlineService.getAll());
        model.addAttribute("airports", airportService.getAll());
        return "admin/flights/create";
    }

    @PostMapping("/create")
    public String createFlight(@ModelAttribute Flight flight,
                               RedirectAttributes redirectAttributes ) {
        flight.setAirline(airlineService.findById(flight.getAirline().getId()));
        flight.setDepartureAirport(airportService.findById(flight.getDepartureAirport().getId()));
        flight.setArrivalAirport(airportService.findById(flight.getArrivalAirport().getId()));

        flightService.save(flight);
        redirectAttributes.addFlashAttribute("message", "Thêm mới thành công");
        return "redirect:/admin/flights";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlight(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        flightService.remove(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        return "redirect:/admin/flights";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Flight flight = flightService.findById(id);
        model.addAttribute("flight", flight);
        model.addAttribute("airlines", airlineService.getAll());
        model.addAttribute("airports", airportService.getAll());
        return "admin/flights/update";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("flight") Flight flight,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("flight", flight);
            return "/admin/flights/update";
        }
        flightService.update(id, flight);
        redirectAttributes.addFlashAttribute("message", "Chỉnh sửa thành công");
        return "redirect:/admin/flights";
    }

}
