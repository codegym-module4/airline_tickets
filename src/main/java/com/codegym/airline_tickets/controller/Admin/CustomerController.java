package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    @Autowired
    public AccountService accountService;

    @GetMapping("/create")
    public String createCustomer() {
        return "admin/customer/create";
    }

    @GetMapping
    public String getListCustomer(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
//        List<Account> accounts = accountService.getAllAccounts(3L);

        try {
            List<Account> accounts = new ArrayList<>();
            Pageable paging = PageRequest.of(page - 1, size);
            Page<Account> pageAcc = accountService.getAllAccounts(3L, paging);

            accounts = pageAcc.getContent();

            model.addAttribute("accounts", accounts);
//            model.addAttribute("currentPage", pageAcc.getNumber() + 1);
//            model.addAttribute("totalItems", pageAcc.getTotalElements());
//            model.addAttribute("totalPages", pageAcc.getTotalPages());
//            model.addAttribute("pageSize", size);
            return "admin/customer/list";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "admin/customer/list";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable int id) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable int id) {
        return null;
    }


}
