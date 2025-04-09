package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.RevenueByDateDto;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.repository.BookingRepository;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getAll() {
        return List.of();
    }

    @Override
    public void save(Booking s) {

    }

    @Override
    public void update(long id, Booking s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Booking findById(long id) {
        return null;
    }

    @Override
    public List<Booking> findByName(String name) {
        return List.of();
    }

    @Override
    public List<RevenueByDateDto> getRevenueByDay(LocalDate start, LocalDate end) {
        List<RevenueByDateDto> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            RevenueByDateDto revenueByDateDto = new RevenueByDateDto();
            revenueByDateDto.setDate(date);

            BigInteger revenue = bookingRepository.getTotalRevenueByDate(date);
            revenueByDateDto.setRevenue(revenue);
            result.add(revenueByDateDto);
        }
        return result;
    }

    @Override
    public List<Booking> findByStatusAndUserId(int status, long userId) {
        return bookingRepository.findByStatusAndUserId(status, userId);
    }

    @Override
    public List<Booking> findByUserId(long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> findByIdIn(List<Long> ids) {
        return bookingRepository.findByIdIn(ids);
    }

    @Override
    public void updateVnPayOrderId(Long id, String vnPayOrderId) {
        bookingRepository.updateVnPayOrderId(id, vnPayOrderId);
    }

    @Override
    public void updateStatusById(Long id, Integer status) {
        bookingRepository.updateStatusById(id, status);
    }

    @Override
    public void updateStatusByVnPayId(String vnpayOrderId, Integer status) {
        bookingRepository.updateStatusByVnPayId(vnpayOrderId, status);
    }
}