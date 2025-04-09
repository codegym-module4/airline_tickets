package com.codegym.airline_tickets.dto;

import lombok.Data;

@Data
public class CountryDTO {

    private String id;
    private String name;
    private String niceName;
    private String iso;
    private String iso3;
    private Integer numCode;
    private Integer phoneCode;
    private String flag;
}
