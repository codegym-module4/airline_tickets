package com.codegym.airline_tickets.schedule;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Ticket;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IFlightSeatService;
import com.codegym.airline_tickets.service.ITicketService;
import com.codegym.airline_tickets.util.PusherEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeleteUnPayBookingSchedule {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private ITicketService ticketService;

    @Autowired
    private IFlightSeatService flightSeatService;

    @Autowired
    private PusherEvent pusherEvent;

    @Scheduled(cron = "0 * * * * *")
    public void runEveryMinute() {
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(30);
        List<Booking> records = bookingService.findByCreatedAtLessThanEqual(timeThreshold, 1);
        if (records.size() > 0) {
            // Xử lý các bản ghi
            records.forEach(record -> {
                List<Ticket> tickets = ticketService.findByBooking(record);
                tickets.forEach(ticket -> {
                    flightSeatService.updateStatusById(ticket.getSeat().getId(), 1);
                    pusherEvent.pusherTrigger("flight." + ticket.getFlight().getId(), "seat-unoccupied", ticket.getSeat());
                });
                ticketService.deleteAll(tickets);
                bookingService.remove(record.getId());
                System.out.println("Đã xóa thành công bản ghi " + record.getId());
            });
        } else {
            System.out.println("Không có bản ghi cần xóa");
        }
    }
}
