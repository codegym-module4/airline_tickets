package com.codegym.airline_tickets.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted_at = Now() WHERE id=?")
@Where(clause = "deleted_at is null")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "ranking")
    private String ranking;

    @Column(name = "citizen_identification")
    private String citizenIdentification;

    @Column(name = "image")
    private String image;

    @Column(name = "deleted_at")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime deletedAt;
}
