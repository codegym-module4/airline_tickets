package com.codegym.airline_tickets.util;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class FormaterCustom {

    public static String withLargeIntegers(BigInteger value) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(value);
    }
}
