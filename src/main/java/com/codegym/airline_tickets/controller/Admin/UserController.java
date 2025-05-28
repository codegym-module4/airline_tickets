package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.CountryDTO;
import com.codegym.airline_tickets.dto.EmailRequest;
import com.codegym.airline_tickets.dto.UserRequestDTO;
import com.codegym.airline_tickets.dto.UserResponseDTO;
import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import com.codegym.airline_tickets.response.TicketResponse;
import com.codegym.airline_tickets.response.UserResponse;
import com.codegym.airline_tickets.service.impl.AccountService;
import com.codegym.airline_tickets.service.impl.FirebaseStorageService;
import com.codegym.airline_tickets.service.impl.UserService;
import com.codegym.airline_tickets.util.EmailService;
import com.codegym.airline_tickets.util.GetCountries;
import com.codegym.airline_tickets.util.ValidationMessage;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
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

    @Autowired
    private TemplateEngine templateEngine;

    @PostMapping("/sendEmail")
    public boolean sendEmail(EmailRequest emailRequest) {
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
        model.addAttribute("listErrorMes", new LinkedHashMap<>());
        model.addAttribute("user", new UserRequestDTO());
        return "admin/customer/create";
    }

    @PostMapping("/create")
    public String createCustomer(@Valid @ModelAttribute("user") UserRequestDTO user,
                                 BindingResult bindingResult,
                                 @RequestParam("imageFile") MultipartFile file,
                                 RedirectAttributes redirectAttributes, Model model) {


        if (bindingResult.hasErrors()) {
            Map<String, String> listErrorsMes = ValidationMessage.getErrorMes(bindingResult);
            model.addAttribute("listErrorMes", listErrorsMes);
            return "admin/customer/create";
        }

        try {
            User newUser = new User();
            String imageUrl = null;

            if (file.isEmpty()) {
                newUser.setImage(imageUrl);
            } else {
                imageUrl = firebaseStorageService.uploadFile(file, user.getFullName());
            }

            newUser.setFullName(user.getFullName());
            newUser.setGender(user.getGender());
            newUser.setPhone(user.getPhone());
            newUser.setCitizenIdentification(user.getCitizenIdentification());
            newUser.setImage(imageUrl);
            newUser.setDob(user.getDob());
            newUser.setAddress(user.getAddress());
            newUser.setNationality(user.getNationality());

            Account existAccount = accountService.findAccountByEmail(user.getEmail());
            if (existAccount != null) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại. Vui lòng chọn email khác!");
                return "redirect:/admin/customer";
            }

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

            boolean isSuccess = sendEmail(emailRequest);
            if (isSuccess) {
                redirectAttributes.addFlashAttribute("message", "Thêm thông tin thành công. Tài khoản đăng nhập đã được gửi tới email!");
                accountService.register(account);
            } else {
                redirectAttributes.addFlashAttribute("error", "Gửi email thất bại.!");
            }

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh: " + e.getMessage());
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
                                 @RequestParam(required = false) String isUpdate,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        model.addAttribute("currentPage", currentPage);
        List<Account> list = new ArrayList<>();

        Page<Account> accountsPage = accountService.findByEmailContaining(email, PageRequest.of(currentPage - 1, pageSize));

        int totalPages = accountsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if (accountsPage.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy email theo yêu cầu!");
            return "redirect:/admin/customer";
        }

        if ("".equals(email)) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy email theo yêu cầu!");
            return "redirect:/admin/customer";
        }
        if (accountsPage == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy email theo yêu cầu!");
            return "redirect:/admin/customer";
        }

        model.addAttribute("accountsPage", accountsPage);

        if (Objects.isNull(isUpdate)) {
            model.addAttribute("message", "Tìm kiếm thành công!");
            return "admin/customer/list";
        }

        if ("true".equals(isUpdate)) {
            model.addAttribute("message", "Cập nhật thông tin thành công!");
            return "admin/customer/list";
        }
        return "admin/customer/list";
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateCustomer(@PathVariable int id) {
        Account account = accountService.findById(id);
        UserResponseDTO userRes = UserResponseDTO.builder()
                .id(account.getUser().getId())
                .fullName(account.getUser().getFullName())
                .email(account.getEmail())
                .gender(account.getUser().getGender())
                .address(account.getUser().getAddress())
                .nationality(account.getUser().getNationality())
                .phone(account.getUser().getPhone())
                .dob(account.getUser().getDob())
                .citizenIdentification(account.getUser().getCitizenIdentification())
                .build();

        List<CountryDTO> countries = GetCountries.getCountries();

        Context context = new Context();
        context.setVariable("user", userRes);
        context.setVariable("countries", countries);
        String html = templateEngine.process("admin/customer/update", context);

        return ResponseEntity.ok().body(
                UserResponse.builder()
                        .status(HttpStatus.OK)
                        .html(html)
                        .userResponse(userRes)
                        .build()
        );

    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("user") UserRequestDTO userReq, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra. Vui lòng thử lại!");
            return "redirect:/admin/customer";
        }
        Account acc = accountService.findAccountByUserId(userReq.getId());
        acc.setEmail(userReq.getEmail());
        accountService.update(acc.getId(), acc);

        User existUser = userService.findById(userReq.getId());
        existUser.setFullName(userReq.getFullName());
        existUser.setGender(userReq.getGender());
        existUser.setAddress(userReq.getAddress());
        existUser.setNationality(userReq.getNationality());
        existUser.setPhone(userReq.getPhone());
        existUser.setDob(userReq.getDob());
        existUser.setCitizenIdentification(userReq.getCitizenIdentification());
        userService.update(userReq.getId(), existUser);

        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!");
        return "redirect:/admin/customer/search?isUpdate=true&email=" + URLEncoder.encode(userReq.getEmail(), StandardCharsets.UTF_8);

    }

    @GetMapping("/delete/{accountId}")
    public String deleteCustomer(@PathVariable("accountId") Long accountId,
                                 RedirectAttributes redirectAttributes) {

        Account account = accountService.findById(accountId);
        firebaseStorageService.deleteFileFromUrl(account.getUser().getImage());
        accountService.remove(accountId);
        userService.remove(account.getUser().getId());
        redirectAttributes.addFlashAttribute("message", "Xoá thành công!");
        return "redirect:/admin/customer";
    }

}
