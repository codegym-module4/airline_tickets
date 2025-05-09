package com.codegym.airline_tickets.response;

import com.codegym.airline_tickets.dto.UserResponseDTO;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private boolean errors = false;
    private HttpStatus status;
    private String message;
    private UserResponseDTO userResponse;
    private String html;
}
