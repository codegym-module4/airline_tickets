package com.codegym.airline_tickets.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormaterDateTime {
    public static LocalDateTime format(String dateString) {
        LocalDate date = LocalDate.parse(dateString); // parse ngày
        LocalDateTime startOfDay = date.atStartOfDay();
        return startOfDay;
    }
}
