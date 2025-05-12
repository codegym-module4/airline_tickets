package com.codegym.airline_tickets.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    
    private Long id;

    @NotEmpty
    private String fullName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;

    private String gender;
    private String phone;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    private String address;
    private String nationality;
    private String citizenIdentification;

}
