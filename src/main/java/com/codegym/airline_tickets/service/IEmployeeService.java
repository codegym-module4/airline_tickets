package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Employee;

import java.util.List;

public interface IEmployeeService extends IService<Employee> {
    Employee updateOrCreate(Employee e);
}
