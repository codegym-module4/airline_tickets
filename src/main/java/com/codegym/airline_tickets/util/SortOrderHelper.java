package com.codegym.airline_tickets.util;

import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortOrderHelper {

    public static Sort.Order createOrder(String sort, String sortProperty) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, sortProperty);

        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);

            if (matcher.find()) {
                String columnName = matcher.group(1);
                String direction = matcher.group(3);
                if ("asc".equalsIgnoreCase(direction)) {
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                } else if ("desc".equalsIgnoreCase(direction)) {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }
        return order;
    }
}