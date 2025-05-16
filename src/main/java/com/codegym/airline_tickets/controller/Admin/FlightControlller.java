package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.service.impl.AirlineService;
import com.codegym.airline_tickets.service.impl.AirportService;
import com.codegym.airline_tickets.service.impl.FlightSeatService;
import com.codegym.airline_tickets.service.impl.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String listFlights(Model model) {
        List<Flight> flightList = flightService.getAll();

        model.addAttribute("flightList", flightList);
        model.addAttribute("totalSeatMap", getTotalSeatMap(flightList));
        return "admin/flights/list";
    }

    @GetMapping("/search")
    public String searchFlights(@RequestParam("type") String type,
                                @RequestParam("keyword") String keyword,
                                Model model) {
        List<Flight> flightList = switch (type) {
            case "code" -> flightService.searchByCode(keyword);
            case "departure" -> flightService.searchByDeparture(keyword);
            case "arrival" -> flightService.searchByArrival(keyword);
            default -> flightService.getAll();
        };

        model.addAttribute("flightList", flightList);
        model.addAttribute("totalSeatMap", getTotalSeatMap(flightList));
        return "admin/flights/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("flights", new Flight());
        model.addAttribute("airlines", airlineService.getAll());
        model.addAttribute("airports", airportService.getAll());
        return "admin/flights/create";
    }

    @PostMapping("/create")
    public String createFlight(@ModelAttribute @Validated Flight flights,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        List<String> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
        }
        if (!flightService.findByCodeIgnoreCase(flights.getCode()).isEmpty()) {
            model.addAttribute("errorCodeExists", "Mã chuyến bay đã tồn tại");
            addCommonAttributes(model, flights);
            return "admin/flights/create";
        }
        if (flights.getArrival_time().isBefore(flights.getDeparture_time())) {
            errors.add("Thời gian đến phải sau thời gian đi");
        }


        if (flights.getDeparture_time().isBefore(LocalDateTime.now())) {
            errors.add("Thời gian đi phải lớn hơn hoặc bằng thời gian hiện tại");
        }

        if (flights.getDepartureAirport().getId().equals(flights.getArrivalAirport().getId())) {
            errors.add("Sân bay đi và đến không được giống nhau");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            System.out.println(errors);
            addCommonAttributes(model, flights);
            return "admin/flights/create";
        }

        // Gán lại đối tượng liên quan trước khi lưu
        setFlight(flights);
        flightService.save(flights);

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
                         @Valid @ModelAttribute("flight") Flight flights,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        List<String> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
        }

        if (flights.getArrival_time().isBefore(flights.getDeparture_time())) {
            errors.add("Thời gian đến phải sau thời gian đi");
        }

        if (flights.getDepartureAirport().getId().equals(flights.getArrivalAirport().getId())) {
            errors.add("Sân bay đi và đến không được giống nhau");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            System.out.println(errors);
            addCommonAttributes(model, flights);
            return "/admin/flights/update";
        }
//        thời gian bay không được thay đổi
        flights.setArrival_time(flightService.findById(id).getArrival_time());
        flights.setDeparture_time(flightService.findById(id).getDeparture_time());

        flightService.update(id, flights);
        redirectAttributes.addFlashAttribute("message", "Chỉnh sửa thành công");
        return "redirect:/admin/flights";
    }


    private void addCommonAttributes(Model model, Flight flight) {
        model.addAttribute("flight", flight);
        model.addAttribute("airlines", airlineService.getAll());
        model.addAttribute("airports", airportService.getAll());
    }

    private Map<Long, Integer> getTotalSeatMap(List<Flight> flightList) {
        Map<Long, Integer> totalSeatMap = new HashMap<>();
        for (Flight flight : flightList) {
            totalSeatMap.put(flight.getId(), flightService.countTotalSeatsByFlight(flight.getId()));
        }
        return totalSeatMap;
    }

    private void setFlight(Flight flight) {
        flight.setAirline(airlineService.findById(flight.getAirline().getId()));
        flight.setDepartureAirport(airportService.findById(flight.getDepartureAirport().getId()));
        flight.setArrivalAirport(airportService.findById(flight.getArrivalAirport().getId()));
    }


}
