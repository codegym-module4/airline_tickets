package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.entity.Ticket;
import com.codegym.airline_tickets.repository.TicketRepository;
import com.codegym.airline_tickets.service.IRoleService;
import com.codegym.airline_tickets.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> getAll() {
        return List.of();
    }

    @Override
    public void save(Ticket s) {

    }

    @Override
    public void update(long id, Ticket s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Ticket findById(long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> findByName(String name) {
        return List.of();
    }

    @Override
    public Ticket findLatest() {
        return ticketRepository.findLatest();
    }

    @Override
    public Ticket updateOrCreate(Ticket t) {
        Long id = t.getId();
        if (id == null) {
            Ticket latest = findLatest();
            long number = latest.getId() == null ? 0 : latest.getId();
            String code = generateNextCode(number);
            t.setCode(code);
        }
        return ticketRepository.save(t);
    }

    @Override
    public List<Ticket> findByBooking(Booking booking) {
        return ticketRepository.findByBooking(booking);
    }

    @Override
    public void deleteAll(List<Ticket> tickets) {
        ticketRepository.deleteAll(tickets);
    }

    @Override
    public List<Ticket> findTicketByBookingId(Long bookingId) {
        return ticketRepository.findTicketByBookingId(bookingId);
    }

    @Override
    public Ticket findByBookingIdAndFlightId(Long bookingId, Long flightId) {
        return ticketRepository.findByBookingIdAndFlightId(bookingId, flightId);
    }

    private static String generateNextCode(long number) {
        if (number == 0) {
            return "VE001";
        }
        number++;
        return String.format("VE%03d", number);
    }
}