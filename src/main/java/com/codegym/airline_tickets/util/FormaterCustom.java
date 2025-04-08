package com.codegym.airline_tickets.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormaterCustom {

    public static String withLargeIntegers(BigInteger value) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(value);
    }

    public static String formatDateResponse (LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);

    }

    public static String formatDayOfWeek(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> "Thứ Hai";
            case TUESDAY -> "Thứ Ba";
            case WEDNESDAY -> "Thứ Tư";
            case THURSDAY -> "Thứ Năm";
            case FRIDAY -> "Thứ Sáu";
            case SATURDAY -> "Thứ Bảy";
            case SUNDAY -> "Chủ Nhật";
        };
    }

    public static String formatPriceVAT (BigInteger price){
        BigDecimal decimalPrice = new BigDecimal(price);
        BigDecimal result = decimalPrice.multiply(new BigDecimal("0.1"));

        return withLargeIntegers(result.toBigInteger().add(price));

    }
    public static String calPriceVAT(BigInteger price){

        BigDecimal decimalPrice = new BigDecimal(price);
        BigDecimal result = decimalPrice.multiply(new BigDecimal("0.1"));
        return withLargeIntegers(result.toBigInteger());
    }

}
