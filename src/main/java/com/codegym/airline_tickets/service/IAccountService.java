package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountService extends IService<Account> {
    Account getAccountByEmail(String email);

    List<Account> getAllAccounts(Long id);

    Page<Account> getAllAccounts(Long id, Pageable pageable);

    Account findAccountByUserId(Long id);

    Account findAccountByEmail(String email);

    Page<Account> findByEmailContaining(String email, Pageable pageable);
}
