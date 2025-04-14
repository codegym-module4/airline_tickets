package com.codegym.airline_tickets.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE tickets SET deleted_at = Now() WHERE id=?")
@Where(clause = "deleted_at is null")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "flight_seat_id")
    private FlightSeat seat;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;

    @Column(name = "phone")
    private String phone;

    @Column(name = "citizen_identification")
    private String citizenIdentification;

    @Column(name = "email")
    private String email;

    @Column(name = "extra_kg")
    private Integer extraKg;

    @Column(name = "price")
    private BigInteger price;

    @Column(name = "customer_type")
    private Integer type;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "deleted_at")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    public Ticket(Long id, Booking booking, Flight flight, FlightSeat flightSeat, String name, String gender, LocalDate dob, String phone, String citizenIdentification, String email, Integer extraKg, BigInteger price, Integer type, String nationality) {
        this.id = id;
        this.booking = booking;
        this.flight = flight;
        this.seat = flightSeat;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.citizenIdentification = citizenIdentification;
        this.email = email;
        this.extraKg = extraKg;
        this.price = price;
        this.type = type;
        this.nationality = nationality;
    }
}
