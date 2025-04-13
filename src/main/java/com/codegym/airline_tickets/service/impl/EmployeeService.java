package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Employee;
import com.codegym.airline_tickets.repository.EmployeeRepository;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void save(Employee s) {
        employeeRepository.save(s);
    }

    @Override
    public void update(long id, Employee s) {

    }

    @Override
    public void remove(Long id) {
        employeeRepository.deleteById(id);

    }

    @Override
    public Employee findById(long id) {
        return employeeRepository.findById(id).get();
    }

    @Override
    public List<Employee> findByName(String name) {
        return List.of();
    }



    public List<Employee> findByCodeContaining(String keyword) {
        return employeeRepository.findByCodeContainingIgnoreCase(keyword);
    }


    public List<Employee> findByFullNameContaining(String keyword) {
        return employeeRepository.findByFullNameContainingIgnoreCase(keyword);
    }
}