package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.config.VnPayConfig;
import com.codegym.airline_tickets.dto.PaymentDTO;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IBookingService;
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

    @Autowired
    private IBookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createPaymentUrl(@ModelAttribute PaymentDTO request, HttpServletRequest req) {
        try {
            List<Booking> list = bookingService.findByIdIn(request.getBookingId());
            if (list.isEmpty() || list.size() != request.getBookingId().size()) {
                Map<String, String> errors = new HashMap<>();
                errors.put("errors", "true");
                errors.put("message", "Các thanh toán không chính xác!! Vui lòng chọn lại");

                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Map<String, String> res = paymentService.createPaymentUrl(req);
            for (Long id: request.getBookingId()) {
                bookingService.updateVnPayOrderId(id, res.get("vnp_TxnRef"));
            }

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
