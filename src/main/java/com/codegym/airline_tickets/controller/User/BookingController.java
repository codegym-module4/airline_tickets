package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.dto.BookingDTO;
import com.codegym.airline_tickets.dto.BookingTicketDTO;
import com.codegym.airline_tickets.dto.CountryDTO;
import com.codegym.airline_tickets.util.GetCountries;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BookingController {

    @GetMapping("/booking/{key}")
    public String index(@PathVariable String key, Model model, HttpSession session) {
        BookingDTO data = new BookingDTO();
        List<BookingTicketDTO> list = new ArrayList<BookingTicketDTO>();
        List<CountryDTO> countries = GetCountries.getCountries();
        Map<String, String> dataConfirm = (Map<String, String>) session.getAttribute("confirm-data" + key);
        int num_of_adult = Integer.parseInt(dataConfirm.get("num_of_adults"));
        int num_of_child = Integer.parseInt(dataConfirm.get("num_of_child"));
        int num_of_baby = Integer.parseInt(dataConfirm.get("num_of_baby"));
        int num_of_ticket = num_of_adult + num_of_child;
        data.setTotalPrice(new BigInteger(dataConfirm.get("total")));
        data.setNumberOfTickets(num_of_ticket);
        if ("ROUND-TRIP".equals(dataConfirm.get("flight_type"))) {
            data.setFlightType(2);
        } else {
            data.setFlightType(1);
        }
        list = generateBookingTickets(num_of_adult, num_of_child, num_of_baby);
        data.setItems(list);
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
