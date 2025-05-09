package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.response.TicketResponse;
import com.codegym.airline_tickets.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/customer")
public class CustomerRestController {

    @Autowired
    public AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<List<Account>> getCustomer(@PathVariable Long id) {
        List<Account> result = accountService.getAllAccounts(id);
        return ResponseEntity.ok(result);
    }





}
