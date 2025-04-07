package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.config.VnPayConfig;
import com.codegym.airline_tickets.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class VnPayController {
    @Autowired
    private IPaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPaymentUrl(HttpServletRequest req) {
        try {
            String paymentUrl = paymentService.createPaymentUrl(req);

            return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
