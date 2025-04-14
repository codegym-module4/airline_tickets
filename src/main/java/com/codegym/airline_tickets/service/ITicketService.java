package com.codegym.airline_tickets.service;


import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Ticket;

public interface ITicketService extends IService<Ticket> {
    Ticket findLatest();

    Ticket updateOrCreate(Ticket t);
}
