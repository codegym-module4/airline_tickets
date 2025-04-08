package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
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
