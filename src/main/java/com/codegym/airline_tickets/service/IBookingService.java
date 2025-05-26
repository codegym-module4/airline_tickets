package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.dto.RevenueByDateDto;
import com.codegym.airline_tickets.entity.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IBookingService extends IService<Booking> {
    List<RevenueByDateDto> getRevenueByDay(LocalDate start, LocalDate end);

    List<Booking> findByStatusAndUserId(int status, long userId);

    List<Booking> findByUserId(long userId);

    List<Booking> findByIdIn(List<Long> ids);

    void updateVnPayOrderId(Long id, String vnPayOrderId);

    void updateStatusById(Long id, Integer status);

    void updateStatusAndPaymentDateByVnPayId(String vnpayOrderId, Integer status, LocalDateTime date);

    Booking findLatest();

    Booking updateOrCreate(Booking b);

    List<Booking> findByCreatedAtLessThanEqual(LocalDateTime time, Integer status);

    List<Booking> getBookingByFlightDate(LocalDate date);

}
