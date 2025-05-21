package com.codegym.airline_tickets.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class CalFlightTime {

    public static String cal(LocalDateTime depart, LocalDateTime arrival) {
        Duration duration = Duration.between(depart, arrival);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        String result = String.format("%d giờ %d phút", hours, minutes);
        return result;
    }

}
