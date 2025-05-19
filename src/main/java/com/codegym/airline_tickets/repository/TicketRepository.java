package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Seat;
import com.codegym.airline_tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT t.* FROM tickets t ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
    Ticket findLatest();

    List<Ticket> findByBooking(Booking b);

    List<Ticket> findTicketByBookingId(Long bookingId);

    Ticket findByBookingIdAndFlightId(Long bookingId, Long flightId);

    @Query("SELECT t from Ticket t where t.flight.id = :flightId AND t.seat.id = :flightSeatId AND t.type = 1 ORDER BY t.id LIMIT 1")
    Ticket findByFlightIdAndSeatId(Long flightId, Long flightSeatId);

    // 1. Tìm vé theo mã đặt chỗ (booking code)
    List<Ticket> findByBooking_Code(String code);

    // 2. Tìm vé theo tên hành khách (không phân biệt hoa thường)
    List<Ticket> findByNameContainingIgnoreCase(String name);

    // 3. Tìm vé theo tên sân bay đi
    List<Ticket> findByFlight_DepartureAirport_NameContainingIgnoreCase(String keyword);

    // 4. Tìm vé theo tên sân bay đến
    List<Ticket> findByFlight_ArrivalAirport_NameContainingIgnoreCase(String keyword);

    // (Tùy chọn) Tìm theo cả sân bay đi hoặc đến chứa keyword
    @Query("SELECT t FROM Ticket t WHERE LOWER(t.flight.departureAirport.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.flight.arrivalAirport.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Ticket> searchByAirportName(@Param("keyword") String keyword);

}
