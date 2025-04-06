package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Transactional
    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND DATE(b.payment_date) = :date")
    BigInteger getTotalRevenueByDate(LocalDate date);
}
