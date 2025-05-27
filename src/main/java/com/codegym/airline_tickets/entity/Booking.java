package com.codegym.airline_tickets.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE booking SET deleted_at = Now() WHERE id=?")
@Where(clause = "deleted_at is null")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "return_flight_id")
    private Flight returnFlight;

    @Column(name = "flight_type")
    private Integer flightType;

    @Column(name = "num_tickets")
    private Integer numTickets;

    @Column(name = "total_price")
    private BigInteger totalPrice;

    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime payment_date;

    @Column(name = "status")
    private Integer status;

    @Column(name = "vnpay_order_id")
    private String vnpayOrderId;

    @Column(name = "is_send_mail_review")
    private Integer isSendMailReview;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    public Booking (Long id, User user, Flight flight, Flight returnFlight, Integer flightType, Integer numTickets, Integer status, BigInteger totalPrice,LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.flight = flight;
        this.returnFlight = returnFlight;
        this.flightType = flightType;
        this.numTickets = numTickets;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }
}
