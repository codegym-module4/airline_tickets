package com.codegym.airline_tickets.response;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import com.codegym.airline_tickets.entity.Flight;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
    private boolean errors = false;
    private HttpStatus status;
    private String message;
    private FlightResponseDTO flightResponseDTO;
    private String html;
}
