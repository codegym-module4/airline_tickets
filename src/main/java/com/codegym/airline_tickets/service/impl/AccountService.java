package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import com.codegym.airline_tickets.repository.AccountRepository;
import com.codegym.airline_tickets.repository.RoleRepository;
import com.codegym.airline_tickets.repository.UserRepository;
import com.codegym.airline_tickets.service.IAccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService, UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void update(long id, Account s) {
        s.setId(id);
        accountRepository.save(s);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account findById(long id) {
        Optional<Account> optional = accountRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Account> findByName(String name) {
        return List.of();
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByNotDeleteEmail(email);
    }

    @Override
    public List<Account> getAllAccounts(Long id) {
        return accountRepository.findAllByDeletedAtIsNullAndRoleId(id);
    }

    @Override
    public Page<Account> getAllAccounts(Long id, Pageable pageable) {

        List<Account> accounts = accountRepository.findAllByDeletedAtIsNullAndRoleId(id);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Account> list;

        if (accounts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, accounts.size());
            list = accounts.subList(startItem, toIndex);
        }
        Page<Account> accountsPage = new PageImpl<>(
                list,
                PageRequest.of(currentPage, pageSize),
                accounts.size()
        );

        return accountsPage;
    }

    @Override
    public Account findAccountByUserId(Long id) {
        return accountRepository.findByUserId(id);
    }

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository.checkAccountEmailExist(email);
    }

    @Override
    public Page<Account> findByEmailContaining(String email, Pageable pageable) {

        List<Account> accounts = accountRepository.findByEmailContains(email, pageable);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Account> list;

        if (accounts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, accounts.size());
            list = accounts.subList(startItem, toIndex);
        }
        Page<Account> accountsPage = new PageImpl<>(
                list,
                PageRequest.of(currentPage, pageSize),
                accounts.size()
        );

        return accountsPage;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByNotDeleteEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("Email không tồn tại");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + account.getRole().getRoleName());

        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        account.getEmail(),
                        account.getPassword(),
                        List.of(authority)
                );
        String fullName = account.getUser() != null ? account.getUser().getFullName() : "Unknown";
        return new UserWithFullName(userDetails, fullName);
    }

    public void register(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole(roleRepository.findById(3L).orElseThrow());

        User user = account.getUser();
        if (user != null) {
            String maxCode = userRepository.findMaxCode();
            int nextNumber = 1;
            if (maxCode != null && maxCode.startsWith("KH")) {
                try {
                    nextNumber = Integer.parseInt(maxCode.substring(2)) + 1;
                } catch (NumberFormatException ignored) {
                }
            }
            String newCode = String.format("KH%03d", nextNumber);
            user.setCode(newCode);
            userRepository.save(user); // Lưu User trước
            account.setUser(user); // Gán User cho Account
        }

        accountRepository.save(account);
    }

    public static class UserWithFullName extends org.springframework.security.core.userdetails.User {
        private final String fullName;

        public UserWithFullName(org.springframework.security.core.userdetails.User user, String fullName) {
            super(user.getUsername(), user.getPassword(), user.getAuthorities());
            this.fullName = fullName;
        }

        public String getFullName() {
            return fullName;
        }
    }


}