package com.codegym.airline_tickets.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE flights SET deleted_at = Now() WHERE id=?")
@Where(clause = "deleted_at is null")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    @NotBlank(message = "Mã chuyến bay không được để trống")
    @Size(min = 1, max = 20, message = "Mã chuyến bay phải có độ dài từ 1 đến 20 ký tự")
    @Pattern(regexp = "^VJ\\d+$", message = "Mã chuyến bay phải bắt đầu bằng 'VJ' và theo sau là các chữ số")
    private String code;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    @NotNull(message = "Hãng hàng không không được để trống")
    private Airline airline;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    @NotNull(message = "Sân bay đi không được để trống")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    @NotNull(message = "Sân bay đến không được để trống")
    private Airport arrivalAirport;

    @Column(name = "departure_time")
    @NotNull(message = "Thời gian khởi hành không được để trống")
    @FutureOrPresent(message = "Thời gian khởi hành phải là thời gian hiện tại hoặc trong tương lai")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime departure_time;

    @Column(name = "arrival_time")
    @NotNull(message = "Thời gian đến không được để trống")
    @FutureOrPresent(message = "Thời gian đến phải là thời gian hiện tại hoặc trong tương lai")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime arrival_time;

    @Column(name = "available_seats")
//    @Min(value = 0, message = "Số ghế có sẵn phải lớn hơn hoặc bằng 0")
    private Integer availableSeats;

    @Column(name = "price")
    @NotNull(message = "Giá vé không được để trống")
    @Min(value = 0, message = "Giá vé phải lớn hơn hoặc bằng 0")
    private BigInteger price;

    @Column(name = "deleted_at")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime deletedAt;
}
