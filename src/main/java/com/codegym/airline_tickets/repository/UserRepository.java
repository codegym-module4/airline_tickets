package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT code FROM users ORDER BY code DESC LIMIT 1", nativeQuery = true)
    String findMaxCode();


    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    User findNotDeletedById(long id);

}
