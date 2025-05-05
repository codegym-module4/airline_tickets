package com.codegym.airline_tickets.response;

import com.codegym.airline_tickets.dto.FlightResponseDTO;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
    private boolean errors = false;
    private HttpStatus status;
    private String message;
    private String url;
    private Object object;
}
