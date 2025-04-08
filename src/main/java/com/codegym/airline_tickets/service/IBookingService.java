package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Airline;
import com.codegym.airline_tickets.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IBookingService extends IService<Booking> {
    List<Booking> findByStatus(int status);

    List<Booking> findByIdIn(List<Long> ids);

    void updateVnPayOrderId(Long id, String vnPayOrderId);

    void updateStatusById(Long id, Integer status);

    void updateStatusByVnPayId(String vnpayOrderId, Integer status);
}
