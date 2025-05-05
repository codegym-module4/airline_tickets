package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.FlightSeatDTO;
import com.codegym.airline_tickets.entity.Col;
import com.codegym.airline_tickets.entity.Row;
import com.codegym.airline_tickets.service.IColService;
import com.codegym.airline_tickets.service.IFlightSeatService;
import com.codegym.airline_tickets.service.IRowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private IColService colService;

    @GetMapping("/flight/{id}")
    public String seat(@PathVariable long id, Model model) {
        List<FlightSeatDTO> lists = flightSeatService.getAllSeatByFlightId(id);
        lists.forEach(seat -> seat.setRowAsInt(Integer.parseInt(seat.getSeatRow())));
        int maxRow = lists.stream()
                .mapToInt(seat -> Integer.parseInt(seat.getSeatRow()))
                .max()
                .orElse(40);

        Map<Integer, Map<String, FlightSeatDTO>> seatMap = new HashMap<>();

        for (FlightSeatDTO seat : lists) {
            seatMap
                    .computeIfAbsent(seat.getRowAsInt(), r -> new HashMap<>())
                    .put(seat.getSeatCol(), seat);
        }
//        List<Col> seatCols = colService.getAll();
        List<String> cols = colService.getAll().stream()
                .map(Col::getAlphabet)
                .collect(Collectors.toList());
        model.addAttribute("cols", cols);
        model.addAttribute("maxRow", maxRow);
        model.addAttribute("seats", lists);
        model.addAttribute("seatMap", seatMap);

        return "admin/flight_seat/seats";
    }
}
