package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByCodeContainingIgnoreCase(String code);
    List<Employee> findByFullNameContainingIgnoreCase(String fullName);
}
