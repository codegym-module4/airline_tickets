package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.entity.Ticket;
import com.codegym.airline_tickets.service.IRoleService;
import com.codegym.airline_tickets.service.ITicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService implements ITicketService {

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
        return null;
    }

    @Override
    public List<Ticket> findByName(String name) {
        return List.of();
    }
}