package com.codegym.airline_tickets.schedule;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SendEmailReview {

    @Autowired
    private IBookingService bookingService;

    @Scheduled(cron = "0 * * * * *")
    public void getYesterdayFlights() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List< Booking> list = bookingService.getBookingByFlightDate(yesterday);

        System.out.println("Số chuyến bay của ngày hôm qua là: " + list.size());
    }
}
