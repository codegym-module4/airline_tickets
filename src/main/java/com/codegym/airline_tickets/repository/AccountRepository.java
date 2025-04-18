package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long> {


    @Query("SELECT a FROM Account a WHERE a.email = :email AND a.deletedAt IS NULL")

    Account findByNotDeleteEmail(String email);


    Account findByEmail(String email);

    Account findByEmployeeId(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE accounts SET employee_id = ?2 WHERE id = ?1", nativeQuery = true)
    void updateEmployeeId(Long accountId, Long employeeId);

}
