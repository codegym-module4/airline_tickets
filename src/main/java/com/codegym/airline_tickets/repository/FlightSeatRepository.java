package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.dto.FlightSeatDTO;
import com.codegym.airline_tickets.entity.FlightSeat;
import com.codegym.airline_tickets.response.SeatAvailable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {

    @Query("SELECT fl from FlightSeat fl where fl.flight.id = :flightId and fl.status = :status")
    List<FlightSeat> findAvailableSeatsByFlight(@Param("flightId") Long flightId, @Param("status") Integer status);

    @Transactional
    @Modifying
    @Query("update FlightSeat fl set fl.status = :status where fl.id = :id")
    void updateSeatStatus(@Param("id") Long id, @Param("status") Integer status);


    @Query("select fl.flight.id, fl.flight.code, count (*) AS seatAvailable " +
            " from FlightSeat fl " +
            "JOIN Flight f on fl.flight.id = f.id" +
            " where fl.flight.id IN (:flightIds) " +
            "and fl.status = 1 " +
            "GROUP BY fl.flight.id")
    List<Object[]> countSeatAvailable (@Param("flightIds") List<Long> flightIds);

    @Query("SELECT COUNT(fs) FROM FlightSeat fs WHERE fs.flight.id = :flightId AND fs.status = 1")
    Integer countSingleFlightSeat(@Param("flightId") Long flightId);

    @Query("select new com.codegym.airline_tickets.dto.FlightSeatDTO(fl.id, f.id, sr.number, sc.alphabet, fl.status) " +
            "from FlightSeat fl " +
            "join Flight f on fl.flight.id = f.id " +
            "join Seat s on s.id = fl.seat.id " +
            "join Row sr on sr.id = s.row.id " +
            "join Col sc on sc.id = s.col.id " +
            "where f.id = :flightId")
    List<FlightSeatDTO> getAllSeatByFlightId(Long flightId);

}
