package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Ticket;
import com.codegym.airline_tickets.service.IAccountService;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.ITicketService;
import com.codegym.airline_tickets.service.impl.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/my-ticket")
public class TicketController {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITicketService ticketService;

    @GetMapping()
    public String myTicket(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountService.getAccountByEmail(email);
        List<Booking> listBooking = bookingService.findByUserId(account.getUser().getId());

        List<Ticket> tickets = new ArrayList<>();

        tickets.addAll(listBooking.stream().map(item -> ticketService.findTicketByBookingId(item.getId()))
                .flatMap(List::stream)
                .collect(toList()));

        model.addAttribute("tickets", tickets);
        return "user/payment/my_ticket";
    }
}
