package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.code FROM User u ORDER BY u.code DESC LIMIT 1")
    String findMaxCode();


    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    User findNotDeletedById(long id);



}
