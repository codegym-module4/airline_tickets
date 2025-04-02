package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.FlightSeat;
import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.service.IFlightSeatService;
import com.codegym.airline_tickets.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    @Override
    public List<Role> getAll() {
        return List.of();
    }

    @Override
    public void save(Role s) {

    }

    @Override
    public void update(long id, Role s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Role findById(long id) {
        return null;
    }

    @Override
    public List<Role> findByName(String name) {
        return List.of();
    }
}