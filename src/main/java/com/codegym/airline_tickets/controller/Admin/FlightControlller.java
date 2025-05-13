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

//    @GetMapping()
//    public String listFlight(Model model) {
//        model.addAttribute("flightList", flightService.getAll());
//        return "admin/flights/list";
//    }

    @GetMapping()
    public String listFlights(Model model) {
        List<Flight> flightList = flightService.getAll();

        // Tạo map để lưu tổng ghế từng chuyến
        Map<Long, Integer> totalSeatMap = new HashMap<>();
        for (Flight flight : flightList) {
            Integer count = flightService.countTotalSeatsByFlight(flight.getId());
            totalSeatMap.put(flight.getId(), count);
        }

        model.addAttribute("flightList", flightList);
        model.addAttribute("totalSeatMap", totalSeatMap); // truyền map xuống Thymeleaf
        return "admin/flights/list";
    }

    @GetMapping("/search")
    public String searchFlights(@RequestParam("type") String type,
                                @RequestParam("keyword") String keyword,
                                Model model) {
        List<Flight> flightList;

        switch (type) {
            case "code":
                flightList = flightService.searchByCode(keyword);
                break;
            case "departure":
                flightList = flightService.searchByDeparture(keyword);
                break;
            case "arrival":
                flightList = flightService.searchByArrival(keyword);
                break;
            default:
                flightList = flightService.getAll();
        }

        model.addAttribute("flightList", flightList);
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

//
//        if (flights.getDeparture_time().isBefore(LocalDateTime.now())) {
//            errors.add("Thời gian đi phải lớn hơn hoặc bằng thời gian hiện tại");
//        }

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
        flights.setAirline(airlineService.findById(flights.getAirline().getId()));
        flights.setDepartureAirport(airportService.findById(flights.getDepartureAirport().getId()));
        flights.setArrivalAirport(airportService.findById(flights.getArrivalAirport().getId()));
        flightService.save(flights);

        redirectAttributes.addFlashAttribute("message", "Thêm mới thành công");
        return "redirect:/admin/flights";
    }

    private void addCommonAttributes(Model model, Flight flight) {
        model.addAttribute("flights", flight);
        model.addAttribute("airlines", airlineService.getAll());
        model.addAttribute("airports", airportService.getAll());
    }

//
//    @PostMapping("/create")
//    public String createFlight(@ModelAttribute @Validated Flight flights,
//                               BindingResult bindingResult,
//                               RedirectAttributes redirectAttributes,
//                               Model model) {
//
//        List<String> errorList = new ArrayList<>();
//
//        if (bindingResult.hasErrors()) {
////            model.addAttribute("errors", bindingResult.getAllErrors());
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            return "admin/flights/create";
//        }
//
//        if (!flightService.searchByCode(flights.getCode()).isEmpty()) {
//            model.addAttribute("errorCodeExists", "Mã chuyến bay đã tồn tại");
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            return "admin/flights/create";
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//        if (flights.getDeparture_time() != null && flights.getDeparture_time().isBefore(now)) {
//            errorList.add("Thời gian khởi hành phải từ hiện tại trở đi.");
//        }
//
//        if (flights.getDeparture_time() != null && flights.getArrival_time() != null &&
//                !flights.getArrival_time().isAfter(flights.getDeparture_time())) {
//            errorList.add("Thời gian đến phải sau thời gian đi.");
//        }
//
//        if (flights.getDepartureAirport() != null && flights.getArrivalAirport() != null &&
//                flights.getDepartureAirport().getId().equals(flights.getArrivalAirport().getId())) {
//            errorList.add("Sân bay đi và đến không được giống nhau.");
//        }
//
//        if (!errorList.isEmpty()) {
//            // Gán lại dữ liệu cho view
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            model.addAttribute("errorMessage", errorList);
//            return "admin/flights/create";
//        }
//
//        // Set lại các liên kết đối tượng chính xác từ DB
//        flights.setAirline(airlineService.findById(flights.getAirline().getId()));
//        flights.setDepartureAirport(airportService.findById(flights.getDepartureAirport().getId()));
//        flights.setArrivalAirport(airportService.findById(flights.getArrivalAirport().getId()));
//
//        flightService.save(flights);
//        redirectAttributes.addFlashAttribute("message", "Thêm mới thành công");
//        return "redirect:/admin/flights";
//    }


//
//    @PostMapping("/create")
//    public String createFlight(@ModelAttribute @Validated Flight flights,
//                               BindingResult bindingResult,
//                               RedirectAttributes redirectAttributes,
//                               Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("errors", bindingResult.getAllErrors());
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            return "admin/flights/create";
//        }
//        if (!flightService.searchByCode(flights.getCode()).isEmpty()) {
//            model.addAttribute("errorCodeExists", "Mã chuyến bay đã tồn tại");
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            return "admin/flights/create";
//        }
//        LocalDateTime now = LocalDateTime.now();
//        if (flights.getDeparture_time().isBefore(now)) {
//            model.addAttribute("errorDeparturePast", "Thời gian khởi hành phải từ hiện tại trở đi");
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            return "admin/flights/create";
//        }
//        if (flights.getDeparture_time().isAfter(flights.getArrival_time())) {
//            model.addAttribute("errorDate", "Ngày khởi hành không được lớn hơn ngày đến");
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            return "admin/flights/create";
//        }
//        if (flights.getDepartureAirport().getId().equals(flights.getArrivalAirport().getId())) {
//            model.addAttribute("errorSameAirport", "Sân bay đi và đến không được giống nhau");
//            model.addAttribute("flights", flights);
//            model.addAttribute("airlines", airlineService.getAll());
//            model.addAttribute("airports", airportService.getAll());
//            return "admin/flights/create";
//        }
//
//        flights.setAirline(airlineService.findById(flights.getAirline().getId()));
//        flights.setDepartureAirport(airportService.findById(flights.getDepartureAirport().getId()));
//        flights.setArrivalAirport(airportService.findById(flights.getArrivalAirport().getId()));
//        flightService.save(flights);
//        redirectAttributes.addFlashAttribute("message", "Thêm mới thành công");
//        return "redirect:/admin/flights";
//    }


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
