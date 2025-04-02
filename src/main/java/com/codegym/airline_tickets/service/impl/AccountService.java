package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.Employee;
import com.codegym.airline_tickets.service.IAccountService;
import com.codegym.airline_tickets.service.IEmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements IAccountService {

    @Override
    public List<Account> getAll() {
        return List.of();
    }

    @Override
    public void save(Account s) {

    }

    @Override
    public void update(long id, Account s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Account findById(long id) {
        return null;
    }

    @Override
    public List<Account> findByName(String name) {
        return List.of();
    }
}