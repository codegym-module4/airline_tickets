package com.codegym.airline_tickets.service;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IPaymentService {
    Map<String, String> createPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException;
}
