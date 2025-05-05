package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.BookingDTO;
import com.codegym.airline_tickets.dto.BookingTicketDTO;
import com.codegym.airline_tickets.entity.*;
import com.codegym.airline_tickets.response.FlightResponse;
import com.codegym.airline_tickets.response.ResponseObject;
import com.codegym.airline_tickets.service.IAccountService;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IFlightSeatService;
import com.codegym.airline_tickets.service.ITicketService;
import com.codegym.airline_tickets.util.PusherEvent;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flight-seat")
public class FlightSeatRestController {

    @Autowired
    private IFlightSeatService flightSeatService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private ITicketService ticketService;

    @Autowired
    private PusherEvent pusherEvent;

    private static final Integer PRICE_FOR_A_KG = 10000;

    @PostMapping("/select")
    public ResponseEntity<?> selectSeat(@RequestParam Map<String, String> requestBody, HttpSession session) {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> dataConfirm = (Map<String, String>) session.getAttribute("confirm-data" + requestBody.get("key"));
        BookingDTO dataBooking = (BookingDTO) session.getAttribute("BookingDTO" + requestBody.get("key"));
        List<Long> ids = requestBody.entrySet().stream()
                .filter(e -> e.getKey().startsWith("seat"))          // Lọc key bắt đầu bằng "seat"
                .map(Map.Entry::getValue)                            // Lấy value
                .map(v -> v == null || v.isEmpty() ? 0 : Long.parseLong(v))  // Nếu rỗng thì thành 0
                .collect(Collectors.toList());
        for (Long id : ids) {
            if (id != 0) {
                FlightSeat flightSeat = flightSeatService.findById(id);
                if (flightSeat.getStatus() != null && flightSeat.getStatus() == 2) {
                    return ResponseEntity.badRequest().body(
                            ResponseObject.builder()
                                    .errors(true)
                                    .status(HttpStatus.BAD_REQUEST)
                                    .message("Ghế " + flightSeat.getSeat().getRow().getNumber() + flightSeat.getSeat().getCol().getAlphabet() + " đã được đặt chỗ quý khách. Vui lòng chọn ghế khác")
                                    .build()
                    );
                }
            }
        }
        List<BookingTicketDTO> items = dataBooking.getItems();
        for (int i = 0; i < ids.size(); i++) {
            if (Objects.equals(requestBody.get("noFlight"), "2") && dataConfirm.get("idArrival") != null && !Objects.equals(dataConfirm.get("idArrival"), "")) {
                if (ids.get(i) == 0) {
                    FlightSeat seat = flightSeatService.getRandomAvailableSeat(ids, Long.parseLong(dataConfirm.get("idArrival")));
                    items.get(i).setIdSeatReturn(seat.getId());
                } else {
                    items.get(i).setIdSeatReturn(ids.get(i));
                }
            } else {
                if (ids.get(i) == 0) {
                    FlightSeat seat = flightSeatService.getRandomAvailableSeat(ids, Long.parseLong(dataConfirm.get("idDepart")));
                    items.get(i).setIdSeatGo(seat.getId());
                } else {
                    items.get(i).setIdSeatGo(ids.get(i));
                }
            }
        }
        dataBooking.setItems(items);
        session.setAttribute("BookingDTO" + requestBody.get("key"), dataBooking);
        result.put("errors", "false");
        if (Objects.equals(requestBody.get("noFlight"), "2")) {
            save(dataBooking, session);
        } else {
            result.put("url", "/booking/" + requestBody.get("key") + "/seat-selection/" + dataBooking.getReturnFlight().getId());
        }

        return ResponseEntity.ok(result);
    }

    @Transactional
    public void save(BookingDTO data, HttpSession session) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.getAccountByEmail(email);
        Map<String, String> dataConfirm = (Map<String, String>) session.getAttribute("confirm-data" + data.getKey());
        int num_of_adult = Integer.parseInt(dataConfirm.get("num_of_adult"));
        int num_of_child = Integer.parseInt(dataConfirm.get("num_of_child"));
        int num_of_seat = num_of_adult + num_of_child;
        Booking booking = new Booking(
                data.getId(),
                account.getUser(),
                data.getFlight(),
                data.getReturnFlight(),
                data.getFlightType(),
                data.getNumberOfTickets(),
                1,
                data.getTotalPrice(),
                LocalDateTime.now()
        );
        Booking res = bookingService.updateOrCreate(booking);
        saveTicket(data, res, res.getFlight(), 1);
        if (res.getReturnFlight() != null) {
            saveTicket(data, res, res.getReturnFlight(), 2);
        }
        session.removeAttribute("confirm-data" + data.getKey());
        session.removeAttribute("data-booking" + data.getKey());
    }

    private void saveTicket(BookingDTO data, Booking res, Flight flight, Integer flightType) {
        List<BookingTicketDTO> items = data.getItems();
        List<FlightSeat> seats = new ArrayList<>();
        int extraKg;
        BigInteger price = new BigInteger("0");
        for (int i = 0; i < items.size(); i++) {
            BookingTicketDTO item = items.get(i);
            FlightSeat s;
            if (item.getCustomerType() != 3) {
                s = flightSeatService.findById((flightType == 1? item.getIdSeatGo() : item.getIdSeatReturn()));
                price = BigInteger.valueOf(100000);
                seats.add(s);
            } else {
                s = seats.get(item.getIndexSeatWith());
                price = flight.getPrice();
            }
            if (item.getCustomerType() == 1) {
                extraKg = (flightType == 2 ? item.getExtraKgReturn() : item.getExtraKgGo());
            } else {
                extraKg = 0;
            }
            Ticket ticket = new Ticket(
                    item.getId(),
                    res,
                    flight,
                    s,
                    item.getFullName(),
                    item.getGender(),
                    item.getBirthDate(),
                    item.getPhone(),
                    item.getCitizenIdentification(),
                    item.getEmail(),
                    extraKg,
                    price.add(BigInteger.valueOf(extraKg * PRICE_FOR_A_KG)),
                    item.getCustomerType(),
                    item.getNationality()
            );
            Ticket result = ticketService.updateOrCreate(ticket);
            flightSeatService.updateStatusById(s.getId(), 2);
            pusherEvent.pusherTrigger("flight." + flight.getId(), "seat-occupied", s.getId());
        }
    }
}
