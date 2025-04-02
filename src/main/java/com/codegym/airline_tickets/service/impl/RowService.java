package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.entity.Row;
import com.codegym.airline_tickets.service.IRoleService;
import com.codegym.airline_tickets.service.IRowService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RowService implements IRowService {

    @Override
    public List<Row> getAll() {
        return List.of();
    }

    @Override
    public void save(Row s) {

    }

    @Override
    public void update(long id, Row s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Row findById(long id) {
        return null;
    }

    @Override
    public List<Row> findByName(String name) {
        return List.of();
    }
}