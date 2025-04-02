package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
