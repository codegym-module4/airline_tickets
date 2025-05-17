package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.FlightSeatDTO;
import com.codegym.airline_tickets.dto.SeatDTO;
import com.codegym.airline_tickets.entity.Col;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.Row;
import com.codegym.airline_tickets.entity.Seat;
import com.codegym.airline_tickets.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/flight-seat")
public class FlightSeatController {

    @Autowired
    private IFlightSeatService flightSeatService;

    @Autowired
    private ISeatService seatService;

    @Autowired
    private IFlightService flightService;

    @GetMapping("/flight/{id}")
    public String seat(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        Flight flight = flightService.findById(id);
        if (flight == null) {
            redirectAttributes.addFlashAttribute("message", "Chuyến bay không tồn tại");
            return "redirect:/admin/flight";
        }
        List<FlightSeatDTO> lists = flightSeatService.getAllSeatByFlightId(id);
        Map<String, Map<String, FlightSeatDTO>> seatMap = new HashMap<>();
        if (!lists.isEmpty()) {
            lists.forEach(seat -> seat.setRowAsInt(Integer.parseInt(seat.getSeatRow())));

            for (FlightSeatDTO seat : lists) {
                seatMap
                        .computeIfAbsent(seat.getSeatRow(), r -> new HashMap<>())
                        .put(seat.getSeatCol(), seat);
            }
        }
        List<SeatDTO> seats = seatService.findAllSeats();
        model.addAttribute("flight", flight);
        model.addAttribute("flight_id", id);
        model.addAttribute("seats", seats);
        model.addAttribute("seatMap", seatMap);
        model.addAttribute("lists", lists);

        return "admin/flight_seat/seats";
    }
}
