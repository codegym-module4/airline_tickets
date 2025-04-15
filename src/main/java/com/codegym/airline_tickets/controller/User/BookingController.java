package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.BookingDTO;
import com.codegym.airline_tickets.dto.BookingTicketDTO;
import com.codegym.airline_tickets.dto.CountryDTO;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.service.IFlightService;
import com.codegym.airline_tickets.util.GetCountries;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BookingController {

    @Autowired
    private IFlightService flightService;

    @GetMapping("/booking/{key}")
    public String index(@PathVariable String key, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("confirm-data" + key) == null) {
            redirectAttributes.addFlashAttribute("messageError", "Thời hạn để đặt vé đã hết!!");
            return "redirect:/";
        }
        BookingDTO data = new BookingDTO();
        data.setKey(key);
        List<BookingTicketDTO> list = new ArrayList<BookingTicketDTO>();
        List<CountryDTO> countries = GetCountries.getCountries();
        Map<String, String> dataConfirm = (Map<String, String>) session.getAttribute("confirm-data" + key);
        int num_of_adult = Integer.parseInt(dataConfirm.get("num_of_adult"));
        int num_of_child = Integer.parseInt(dataConfirm.get("num_of_child"));
        int num_of_baby = Integer.parseInt(dataConfirm.get("num_of_baby"));
        int num_of_ticket = num_of_adult + num_of_child + num_of_baby;
        data.setTotalPrice(new BigInteger(dataConfirm.get("total")));
        data.setNumberOfTickets(num_of_ticket);
        if ("ROUND-TRIP".equals(dataConfirm.get("flight_type"))) {
            data.setFlightType(2);
        } else {
            data.setFlightType(1);
        }
        long idDepart = Long.parseLong(dataConfirm.get("idDepart"));
        Flight flight = flightService.findById(idDepart);
        data.setFlight(flight);
        if (dataConfirm.get("idArrival") != null && !Objects.equals(dataConfirm.get("idArrival"), "")) {
            long idArrival = Long.parseLong(dataConfirm.get("idArrival"));
            Flight returnFlight = flightService.findById(idArrival);
            data.setReturnFlight(returnFlight);
        }
        list = generateBookingTickets(num_of_adult, num_of_child, num_of_baby);
        data.setItems(list);
        BigInteger total = new BigInteger(dataConfirm.get("total"));
        data.setTotalPrice(total);
        model.addAttribute("countries", countries);
        model.addAttribute("data", data);

        return "user/booking/index";
    }

    public static List<BookingTicketDTO> generateBookingTickets(int numAdult, int numChild, int numBaby) {
        // Sử dụng Stream.concat() kết hợp với map để tạo các BookingTicketDTO
        List<BookingTicketDTO> tickets = Stream
                .concat(
                        generateTickets(numAdult, 1), // Người lớn
                        generateTickets(numChild, 2)  // Trẻ em
                )
                .collect(Collectors.toList());

        // Nếu có em bé, thêm stream em bé vào danh sách
        tickets.addAll(generateTickets(numBaby, 3).collect(Collectors.toList()));

        return tickets;
    }

    public static Stream<BookingTicketDTO> generateTickets(int count, int customerType) {
        return Stream.generate(() -> {
            BookingTicketDTO dto = new BookingTicketDTO();
            dto.setCustomerType(customerType);
            return dto;
        }).limit(count);
    }
}
