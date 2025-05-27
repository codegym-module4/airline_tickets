package com.codegym.airline_tickets.schedule;

import com.codegym.airline_tickets.dto.UserAccountDTO;
import com.codegym.airline_tickets.dto.UserResponseDTO;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IUserService;
import com.codegym.airline_tickets.service.impl.BookingEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class SendEmailReview {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private BookingEmailService emailService;

    @Autowired
    private IUserService userService;

    @Scheduled(cron = "0 * * * * *")
    public void getYesterdayFlights() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Booking> list = bookingService.getBookingByFlightDate(yesterday);

        if (list.size() > 0) {
            UserAccountDTO user;
            for (Booking booking : list) {
                user = userService.findUserAccountById(booking.getUser().getId());
                try {
                    emailService.sendHtmlEmail(
                            user.getEmail(),
                            "Đánh giá chất lượng chuyến bay",
                            booking
                    );
                    booking.setIsSendMailReview(1);
                    bookingService.save(booking);
                } catch (Exception e) {

                }
            }
        } else {
            System.out.println("Không có đặt vé");
        }
    }
}
