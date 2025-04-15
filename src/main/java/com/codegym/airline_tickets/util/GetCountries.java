package com.codegym.airline_tickets.util;

import com.codegym.airline_tickets.dto.CountryDTO;
import com.codegym.airline_tickets.response.CountryResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetCountries {

    public static List<CountryDTO> getCountries() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://open.oapi.vn/location/countries";

        CountryResponse response = restTemplate.getForObject(url, CountryResponse.class);

        List<CountryDTO> countries = response.getData();

        return countries;
    }
}
