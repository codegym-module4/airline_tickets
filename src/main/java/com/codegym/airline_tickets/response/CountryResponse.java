package com.codegym.airline_tickets.response;

import com.codegym.airline_tickets.dto.CountryDTO;
import lombok.Data;

import java.util.List;

@Data
public class CountryResponse {
    private Integer total;
    private List<CountryDTO> data;
}
