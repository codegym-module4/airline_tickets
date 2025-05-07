package com.codegym.airline_tickets.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    private String fullName;
    private String gender;
    private String phone;
    private String email;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;

    private String address;
    private String nationality;
    private String citizenIdentification;
    private String image;
}
