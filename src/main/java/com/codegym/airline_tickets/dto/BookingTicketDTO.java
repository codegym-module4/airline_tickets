package com.codegym.airline_tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingTicketDTO {

    private Long id;
    private String fullName;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private Integer extraKg;
    private String nationality;
    private String email;
    private String citizenIdentification;
    private Integer customerType;
}
