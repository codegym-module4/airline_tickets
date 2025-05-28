package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Log;
import com.codegym.airline_tickets.repository.LogRepository;
import com.codegym.airline_tickets.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService implements ILogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public void save(Log s) {
        logRepository.save(s);
    }


    @Override
    public void update(long id, Log s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Log findById(long id) {
        return null;
    }

    @Override
    public List<Log> findByName(String name) {
        return List.of();
    }

    @Override
    public List<Log> getAll() {
        return List.of();
    }
}
