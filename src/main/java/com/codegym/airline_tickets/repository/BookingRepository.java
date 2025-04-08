package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Transactional
    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND DATE(b.payment_date) = :date")
    BigInteger getTotalRevenueByDate(LocalDate date);

    List<Booking> findByStatus(Integer status);

    List<Booking> findByIdIn(List<Long> ids);

    @Modifying
    @Transactional
    @Query("update Booking b set b.vnpayOrderId = ?2 where b.id = ?1")
    void updateVnPayOrderId(Long id, String vnpayOrderId);

    @Modifying
    @Transactional
    @Query("update Booking b set b.status = ?2 where b.id = ?1")
    void updateStatusById(Long id, Integer status);

    @Modifying
    @Transactional
    @Query("update Booking b set b.status = ?2 where b.vnpayOrderId = ?1")
    void updateStatusByVnPayId(String vnpayOrderId, Integer status);
}
