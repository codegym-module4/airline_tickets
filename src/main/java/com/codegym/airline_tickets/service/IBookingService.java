package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.dto.RevenueByDateDto;
import com.codegym.airline_tickets.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService extends IService<Booking> {
    List<RevenueByDateDto> getRevenueByDay(LocalDate start, LocalDate end);

}
