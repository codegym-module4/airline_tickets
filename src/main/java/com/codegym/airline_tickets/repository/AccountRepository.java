package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {


    @Query("SELECT a FROM Account a WHERE a.email = :email AND a.deletedAt IS NULL")
    Account findByNotDeleteEmail(String email);


    @Query(value = "SELECT a.* FROM accounts a where a.email = :email", nativeQuery = true)
    Optional<Account> findByEmail(String email);

    Optional<Account> findByEmployeeId(Long id);

    @Query(value = "SELECT * FROM accounts WHERE deleted_at IS NULL AND role_id = :id ORDER BY id DESC", nativeQuery = true)
    List<Account> findAllByDeletedAtIsNullAndRoleId(@Param("id") Long id);


    Page<Account> findAllByDeletedAtIsNullAndRoleId(Long id, Pageable pageable);

    Account findByUserId(Long id);

    @Query(value = "SELECT * FROM accounts WHERE email = :email", nativeQuery = true)
    Account checkAccountEmailExist(@Param("email") String email);

    List<Account> findByEmailContains(String email, Pageable pageable);

}
