package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.dto.EmailRequest;
import com.codegym.airline_tickets.dto.EmailRequestTicket;
import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.dto.TicketResponseDTO;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.Ticket;
import com.codegym.airline_tickets.response.ResponseObject;
import com.codegym.airline_tickets.response.TicketResponse;
import com.codegym.airline_tickets.service.impl.TicketService;
import com.codegym.airline_tickets.util.CalFlightTime;
import com.codegym.airline_tickets.util.EmailService;
import com.codegym.airline_tickets.util.FormaterCustom;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket")
public class TicketDetailController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicketByBookingId(@PathVariable("ticketId") Long ticketId) {

        Ticket ticket = ticketService.findById(ticketId);
        Flight flight = ticket.getFlight();
        FlightResponseDTO flightRes = FlightResponseDTO.builder()
                .id(flight.getId())
                .flightCode(flight.getCode())
                .airlineName(flight.getAirline().getName())
                .departureTime(flight.getDeparture_time())
                .arrivalTime(flight.getArrival_time())
                .price(FormaterCustom.withLargeIntegers(flight.getPrice()))
                .departureAirportCity(flight.getDepartureAirport().getCity())
                .arrivalAirportCity(flight.getArrivalAirport().getCity())
                .departureAirportName(flight.getDepartureAirport().getName())
                .arrivalAirportName(flight.getArrivalAirport().getName())
                .departureIATA(flight.getDepartureAirport().getIATA())
                .arrivalIATA(flight.getArrivalAirport().getIATA())
                .build();

        String result = CalFlightTime.cal(flightRes.getDepartureTime(), flightRes.getArrivalTime());

        LocalDateTime timeToBoard = flightRes.getDepartureTime().minusMinutes(50);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


        if (ticket != null) {
            TicketResponseDTO ticketResponseDTO = TicketResponseDTO.builder()
                    .code(ticket.getCode())
                    .seat(ticket.getSeat())
                    .flight(flightRes)
                    .name(ticket.getName())
                    .flightTime(result)
                    .timeToBoard(timeToBoard.format(formatter))
                    .build();

            Context context = new Context();
            context.setVariable("ticket", ticketResponseDTO);
            String html = templateEngine.process("user/payment/ticket_detail", context);

            return ResponseEntity.ok().body(
                    TicketResponse.builder()
                            .status(HttpStatus.OK)
                            .html(html)
                            .ticketResponseDTO(ticketResponseDTO)
                            .build()
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getTicketBySeat")
    public ResponseEntity<ResponseObject> getTicketBySeat(@RequestParam Map<String, String> request) {
        Long flightId = Long.parseLong(request.get("flight_id"));
        Long flightSeatId = Long.parseLong(request.get("flight_seat_id"));
        Ticket ticket = ticketService.findByFlightIdAndSeatId(flightId, flightSeatId);
        if (ticket == null) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .errors(true)
                            .message("Không tìm thấy thông tin vé cho ghế ngồi")
                            .build()
            );
        }

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .object(ticket)
                        .build()
        );
    }

    @GetMapping("/send-email/{ticketId}")
    public ResponseEntity<?> sendEmail(@PathVariable("ticketId") Long ticketId) {

        Ticket ticket = ticketService.findById(ticketId);

        Flight flight = ticket.getFlight();
        FlightResponseDTO flightRes = FlightResponseDTO.builder()
                .id(flight.getId())
                .flightCode(flight.getCode())
                .airlineName(flight.getAirline().getName())
                .departureTime(flight.getDeparture_time())
                .arrivalTime(flight.getArrival_time())
                .price(FormaterCustom.withLargeIntegers(flight.getPrice()))
                .departureAirportCity(flight.getDepartureAirport().getCity())
                .arrivalAirportCity(flight.getArrivalAirport().getCity())
                .departureAirportName(flight.getDepartureAirport().getName())
                .arrivalAirportName(flight.getArrivalAirport().getName())
                .departureIATA(flight.getDepartureAirport().getIATA())
                .arrivalIATA(flight.getArrivalAirport().getIATA())
                .build();

        String result = CalFlightTime.cal(flightRes.getDepartureTime(), flightRes.getArrivalTime());

        LocalDateTime timeToBoard = flightRes.getDepartureTime().minusMinutes(50);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        if (ticket != null) {
            TicketResponseDTO ticketResponseDTO = TicketResponseDTO.builder()
                    .code(ticket.getCode())
                    .seat(ticket.getSeat())
                    .flight(flightRes)
                    .name(ticket.getName())
                    .flightTime(result)
                    .timeToBoard(timeToBoard.format(formatter))
                    .build();

            Context context = new Context();
            EmailRequestTicket emailRequest = new EmailRequestTicket();
            emailRequest.setTo(ticket.getEmail());
            emailRequest.setSubject("[Vietjetair.com] Vé điện tử Vietjetair của quý khách");
            emailRequest.setTicket(ticketResponseDTO);


            // Set variables for the template from the POST request data
            context.setVariable("ticket", emailRequest.getTicket());
            context.setVariable("subject", emailRequest.getSubject());

            try {
                emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), "/user/email/email_ticket", context);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.ok().body("Send email success");
        } else {
            return ResponseEntity.badRequest().body("Send email failed");
        }

    }


}
