package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {


    @Query(value = "SELECT flight from Flight flight  " +
            "JOIN Airport departure ON flight.departureAirport.id = departure.id " +
            "JOIN Airport arrival  ON flight.arrivalAirport.id = arrival.id " +
            "JOIN Airline airline ON flight.airline.id = airline.id " +
            "WHERE flight.departureAirport.name = :departure " +
            "AND flight.arrivalAirport.name = :arrival " +
            "AND DATE(flight.departure_time) = :time")
    Page<Flight> searchByKeyword(@Param("departure") String departure,
                                 @Param("arrival") String arrival,
                                 @Param("time") LocalDate time,
                                 Pageable pageable);

    @Query("SELECT f from Flight f where f.id = :id and f.deletedAt is null")
    Flight findNotDeletedById(Long id);

    @Query(value = "SELECT flight from Flight flight  " +
            "JOIN Airport departure ON flight.departureAirport.id = departure.id " +
            "JOIN Airport arrival  ON flight.arrivalAirport.id = arrival.id " +
            "JOIN Airline airline ON flight.airline.id = airline.id " +
            "WHERE flight.departureAirport.city = :departure " +
            "AND flight.arrivalAirport.name = :arrival " +
            "AND flight.price = :price")
    Page<Flight> searchFightHotDeal(@Param("departure") String departure,
                                    @Param("arrival") String arrival,
                                    @Param("price") int price,
                                    Pageable pageable);

//trả về 1 bản ghi duy nhất hoặc trống
    Optional<Flight> findByCodeIgnoreCase(String code);
//    boolean existsByCodeIgnoreCase(String code);

    List<Flight> findByCodeContainingIgnoreCase(String code);

    @Query("SELECT f FROM Flight f " +
            "WHERE LOWER(f.arrivalAirport.name) LIKE LOWER(CONCAT('%', :arrival, '%'))")
    List<Flight> searchByArrivalAirportName(@Param("arrival") String arrival);

    @Query("SELECT f FROM Flight f " +
            "WHERE LOWER(f.departureAirport.name) LIKE LOWER(CONCAT('%', :departure, '%'))")
    List<Flight> searchByDepartureAirportName(@Param("departure") String departure);


//    @Query("SELECT f FROM Flight f " +
//            "WHERE (:departure IS NULL OR LOWER(f.departureAirport.city) LIKE LOWER(CONCAT('%', :departure, '%'))) " +
//            "AND (:arrival IS NULL OR LOWER(f.arrivalAirport.city) LIKE LOWER(CONCAT('%', :arrival, '%')))")
//    List<Flight> searchByDepartureAndArrival(@Param("departure") String departure,
//                                             @Param("arrival") String arrival);


}
