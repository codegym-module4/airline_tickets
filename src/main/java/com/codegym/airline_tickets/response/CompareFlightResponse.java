package com.codegym.airline_tickets.response;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.dto.UserResponseDTO;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompareFlightResponse {
    private boolean errors = false;
    private HttpStatus status;
    private String message;
    private List<FlightResponseDTO> flightResponseDTO;
    private String html;
}
