package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Col;
import com.codegym.airline_tickets.entity.Row;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RowRepository extends JpaRepository<Row, Long> {

}
