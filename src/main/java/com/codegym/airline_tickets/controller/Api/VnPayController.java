package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.config.VnPayConfig;
import com.codegym.airline_tickets.dto.PaymentDTO;
import com.codegym.airline_tickets.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createPaymentUrl(@ModelAttribute PaymentDTO request, HttpServletRequest req) {
        try {
            Map<String, String> res = paymentService.createPaymentUrl(req);

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
