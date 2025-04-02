package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Col;
import com.codegym.airline_tickets.entity.Row;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RowRepository extends JpaRepository<Row, Long> {

}
