package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.entity.Flight;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightSeatDTO {
    private Long id;
    private Long flightId;
    private String seatRow;
    private String seatCol;
    private Integer status;

    private Integer rowAsInt;

    public FlightSeatDTO(Long id, Long flightId, String seatRow, String seatCol, Integer status) {
        this.id = id;
        this.flightId = flightId;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.status = status;
    }
}
