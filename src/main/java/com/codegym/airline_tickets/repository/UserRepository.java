package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
