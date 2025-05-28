package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    BigInteger getTotalRevenueByDate(@Param("date") LocalDate date);

    @Transactional
    @Query("SELECT DISTINCT b.user " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND DATE(b.payment_date) = :date AND b.status = 2")
    List<User> getUserIdByDate(@Param("date") LocalDate date);

    @Transactional
    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND b.user.id = :id AND DATE(b.payment_date) = :date AND b.status = 2")
    BigInteger getRevenueByUserIdAndDate(@Param("id") Long id,
                                         @Param("date") LocalDate date
    );

    @Transactional
    @Query("SELECT b.numTickets " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND b.user.id = :id AND DATE(b.payment_date) = :date AND b.status = 2")
    int getNumberOfTicketsByUserIdAndDate(@Param("id") Long id,
                                          @Param("date") LocalDate date
    );


    @Transactional
    @Query("SELECT DISTINCT b.flight, b.returnFlight " +
            "FROM Booking b " +
            "LEFT JOIN b.returnFlight " +
            "WHERE b.deletedAt IS NULL " +
            "AND DATE(b.payment_date) = :date AND b.status = 2")
    List<Object[]> getFlightAndReturnFlightPairsByDate(@Param("date") LocalDate date);

    @Transactional
    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND b.flight.id = :flightId " +
            "AND ((:returnFlightId IS NULL AND b.returnFlight IS NULL) " +
            "OR (b.returnFlight.id = :returnFlightId)) " +
            "AND DATE(b.payment_date) = :date AND b.status = 2")
    BigInteger getRevenueByFlightAndReturnFlightAndDate(@Param("flightId") Long flightId,
                                                        @Param("returnFlightId") Long returnFlightId,
                                                        @Param("date") LocalDate date
    );

    @Transactional
    @Query("SELECT DISTINCT b.flightType " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND DATE(b.payment_date) = :date AND b.status = 2")
    List<Integer> getFlightTypeByDate(@Param("date") LocalDate date);

    @Transactional
    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Booking b " +
            "WHERE b.deletedAt IS NULL " +
            "AND b.flightType = :flight_type AND DATE(b.payment_date) = :date AND b.status = 2")
    BigInteger getRevenueByFlightTypeIdAndDate(@Param("flight_type") Integer flight_type,
                                               @Param("date") LocalDate date
    );


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

    @Query(value = "SELECT * FROM booking b ORDER BY b.id DESC LIMIT 1", nativeQuery = true)
    Booking findLatest();

    List<Booking> findByCreatedAtLessThanEqualAndStatus(LocalDateTime time, Integer status);

    @Query(value =  "SELECT b from Booking b " +
                    "LEFT JOIN Flight f ON f.id = b.flight.id " +
                    "LEFT JOIN Flight as rf ON rf.id = b.returnFlight.id " +
                    "where (DATE(f.arrival_time) = :date OR DATE(rf.arrival_time) = :date) AND b.isSendMailReview = 0")
    List<Booking> getBookingByFlightDate(LocalDate date);
}