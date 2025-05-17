package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.entity.Row;
import com.codegym.airline_tickets.repository.RowRepository;
import com.codegym.airline_tickets.service.IRoleService;
import com.codegym.airline_tickets.service.IRowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RowService implements IRowService {

    @Autowired
    private RowRepository rowRepository;

    @Override
    public List<Row> getAll() {
        return rowRepository.findAll();
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