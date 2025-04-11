package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Employee;

import java.util.List;

public interface IEmployeeService extends IService<Employee> {
    List<Employee> findByCodeContaining(String keyword);
    List<Employee> findByFullNameContaining(String keyword);
}
