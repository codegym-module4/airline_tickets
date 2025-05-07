package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.CountryDTO;
import com.codegym.airline_tickets.dto.EmailRequest;
import com.codegym.airline_tickets.dto.UserRequestDTO;
import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import com.codegym.airline_tickets.response.TicketResponse;
import com.codegym.airline_tickets.service.impl.AccountService;
import com.codegym.airline_tickets.service.impl.FirebaseStorageService;
import com.codegym.airline_tickets.service.impl.UserService;
import com.codegym.airline_tickets.util.EmailService;
import com.codegym.airline_tickets.util.GetCountries;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("/admin/customer")
public class UserController {

    @Autowired
    public AccountService accountService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
//
//    @PostMapping("/api/sendEmail")
//    public ResponseEntity<?> sendEmailtest(@RequestBody EmailRequest emailRequest, RedirectAttributes redirectAttributes) {
//        Context context = new Context();
//        // Set variables for the template from the POST request data
//        context.setVariable("name", emailRequest.getName());
//        context.setVariable("account", emailRequest.getAccount());
//        context.setVariable("subject", emailRequest.getSubject());
//        try {
//            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), "/admin/email/email_template", context);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return null;
//        }
//    }


    @PostMapping("/sendEmail")
    public boolean sendEmail(EmailRequest emailRequest, RedirectAttributes redirectAttributes) {
        Context context = new Context();
        // Set variables for the template from the POST request data
        context.setVariable("name", emailRequest.getName());
        context.setVariable("account", emailRequest.getAccount());
        context.setVariable("subject", emailRequest.getSubject());
        try {
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), "/admin/email/email_template", context);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<CountryDTO> countries = GetCountries.getCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("user", new UserRequestDTO());
        return "admin/customer/create";
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute("user") UserRequestDTO user,
                                 BindingResult bindingResult,
                                 @RequestParam("imageFile") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            bindingResult.rejectValue("imageURL", "file.empty", "Vui lòng chọn ảnh cho khách hàng.");
        }

        if (bindingResult.hasErrors()) {
            return "admin/customer/create";
        }

        try {
            String imageUrl = firebaseStorageService.uploadFile(file, user.getFullName());
            User newUser = new User();
            newUser.setFullName(user.getFullName());
            newUser.setGender(user.getGender());
            newUser.setPhone(user.getPhone());
            newUser.setCitizenIdentification(user.getCitizenIdentification());
            newUser.setImage(imageUrl);
            newUser.setDob(user.getDob());
            newUser.setAddress(user.getAddress());
            newUser.setNationality(user.getNationality());

            Account account = new Account();

            // Gửi mail tài khoản cho khách hàng
            account.setEmail(user.getEmail());
            String randomNum = String.format("%06d", (int) (Math.random() * 1000000));
            account.setPassword(randomNum);
            account.setUser(newUser);
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(user.getEmail());
            emailRequest.setSubject("Thông tin tài khoản đăng nhập");
            emailRequest.setAccount(account);
            emailRequest.setName(user.getFullName());
            boolean isSuccess = sendEmail(emailRequest, redirectAttributes);
            if (isSuccess) {
                redirectAttributes.addFlashAttribute("message", "Thêm khách hàng thành công!. Tài khoản đăng nhập đã được gửi email cho khách hàng.!");
                accountService.register(account);
            } else {
                redirectAttributes.addFlashAttribute("error", "Gửi email thất bại.!");
            }

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi tải ảnh: " + e.getMessage());
        }

        return "redirect:/admin/customer";
    }

    @GetMapping
    public String getListCustomer(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        model.addAttribute("currentPage", currentPage);
        Page<Account> accountsPage = accountService.getAllAccounts(3L, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("accountsPage", accountsPage);

        int totalPages = accountsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/customer/list";
    }

    @GetMapping("/search")
    public String searchCustomer(Model model, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        model.addAttribute("currentPage", currentPage);
        List<Account> list = new ArrayList<>();

        Account accounts = accountService.getAccountByEmail(email);
        if (email.equals("")) {
            return "redirect:/admin/customer";
        }
        if (accounts == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng theo yêu cầu!");
            return "redirect:/admin/customer";
        }
        list.add(accounts);
        Page<Account> pages = new PageImpl<>(list);
        model.addAttribute("accountsPage", pages);
        redirectAttributes.addFlashAttribute("message", "Tìm kiếm thành công!");
        return "admin/customer/list";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable int id) {
        return null;
    }

    @GetMapping("/delete/{accountId}")
    public String deleteCustomer(@PathVariable("accountId") Long accountId,
                                 RedirectAttributes redirectAttributes) {

        Account account = accountService.findById(accountId);
        firebaseStorageService.deleteFileFromUrl(account.getUser().getImage());
//        account.setEmail("");
//        account.setDeletedAt(LocalDateTime.now());
//        accountService.update(accountId, account);
        accountService.remove(accountId);
        userService.remove(account.getUser().getId());
        redirectAttributes.addFlashAttribute("message", "Xoá thành công!");
        return "redirect:/admin/customer";
    }

}
