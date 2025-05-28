package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Booking;

public interface IEmailService {
    void sendHtmlEmail(String to, String subject, Booking booking);
}
