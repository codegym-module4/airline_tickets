package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Employee;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByCodeContainingIgnoreCase(String code);
    List<Employee> findByFullNameContainingIgnoreCase(String fullName);

    Optional<Employee> findByCode(@NotEmpty(message = "Mã nhân viên không được để trống") String code);
}
