package com.codegym.airline_tickets.util;

import com.codegym.airline_tickets.dto.FlightRequestDTO;


import java.time.LocalDate;
import java.util.Map;

public class BuildFlightRequest {
    public static FlightRequestDTO build (Map<String, String> request, String sortProperty ){
        String type = request.get("type");
        String departureAirport = request.get("departureAirport");
        String arrivalAirport = request.get("arrivalAirport");
        String arrivalAirportOneWay = request.get("arrivalAirportOneWay");
        String departureTime = request.get("departureTime");
        String arrivalTime = request.get("arrivalTime");
        String price = request.get("price");
        boolean isHotdeal = Boolean.parseBoolean(request.get("isHotdeal")) ;

        if(isHotdeal){
            return FlightRequestDTO.builder()
                    .type(type == null ? "ONEWAY" : type)
                    .departureAirport(departureAirport)
                    .arrivalAirport(arrivalAirport)
                    .arrivalAirportOneWay(arrivalAirportOneWay)
                    .departureTime(LocalDate.now())
                    .arrivalTime(LocalDate.now())
                    .price(Integer.parseInt(price))
                    .sortProperty(sortProperty)
                    .build();
        }else {

            if (departureAirport.isEmpty() || arrivalAirport.isEmpty() || departureTime.isEmpty() || arrivalTime.isEmpty()) {
                return null;
            }

            return FlightRequestDTO.builder()
                    .type(type.isEmpty() ? "ONEWAY" : type)
                    .departureAirport(departureAirport)
                    .arrivalAirport(arrivalAirport)
                    .arrivalAirportOneWay(arrivalAirportOneWay)
                    .departureTime(LocalDate.parse(departureTime))
                    .arrivalTime(arrivalTime.isEmpty() ? null : LocalDate.parse(arrivalTime))
                    .price(null)
                    .sortProperty(sortProperty)
                    .build();
        }
    }
}
