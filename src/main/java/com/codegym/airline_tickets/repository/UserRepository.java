package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id = ?1 and u.deletedAt is not null")
    User findNotDeletedById(Long id);
}
