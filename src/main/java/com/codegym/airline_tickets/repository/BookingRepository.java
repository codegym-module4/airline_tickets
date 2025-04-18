package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Transactional
    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND DATE(b.payment_date) = :date AND b.status = 2")
    BigInteger getTotalRevenueByDate(LocalDate date);

    @Query("select b from Booking b where b.status = ?1 and b.user.id = ?2 and b.deletedAt is null")
    List<Booking> findByStatusAndUserId(Integer status, Long userid);

    @Query("select b from Booking b where b.user.id = ?1 and b.deletedAt is null")
    List<Booking> findByUserId(Long userid);

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
    @Query("update Booking b set b.status = ?2, b.payment_date = :date where b.vnpayOrderId = ?1")
    void updateStatusAndPaymentDateByVnPayId(String vnpayOrderId, Integer status, LocalDateTime date);

    @Query("SELECT b FROM Booking b ORDER BY b.id DESC LIMIT 1")
    Booking findLatest();

    List<Booking> findByCreatedAtLessThanEqualAndStatus(LocalDateTime time, Integer status);
}
