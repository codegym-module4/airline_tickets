package com.codegym.airline_tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirlineTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineTicketsApplication.class, args);
	}

}
