package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r from Review r where r.booking.id = :bookId ORDER BY r.id LIMIT 1")
    Review findByBookingId(long bookId);
}
