package com.codegym.airline_tickets.service;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IPaymentService {
    String createPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException;
}
