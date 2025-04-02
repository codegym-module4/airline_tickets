package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
