package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.dto.SeatDTO;
import com.codegym.airline_tickets.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("select new com.codegym.airline_tickets.dto.SeatDTO(s.id, sr.number, sc.alphabet) " +
            "from Seat s " +
            "join Row sr on s.row.id = sr.id " +
            "join Col sc on s.col.id = sc.id " +
            "order by sr.id, sc.id")
    List<SeatDTO> findAllSeats();
}
