package com.codegym.airline_tickets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountDTO {
    private Long id;

    private String fullName;

    private LocalDate dob;

    private String gender;

    private String phone;

    private String email;

    private String address;

    private String nationality;

    private String citizenIdentification;
}
