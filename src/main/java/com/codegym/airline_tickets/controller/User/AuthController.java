package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import com.codegym.airline_tickets.repository.UserRepository;
import com.codegym.airline_tickets.service.impl.AccountService;
import com.codegym.airline_tickets.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Lưu mã xác nhận tạm thời trong Map (email -> code)
    private final Map<String, String> resetCodes = new HashMap<>();

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        Account account = new Account();
        account.setUser(new User());
        model.addAttribute("account", account);
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("account") @Valid Account account,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (accountService.getAccountByEmail(account.getEmail()) != null) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }
        accountService.register(account);
        User user = account.getUser();
        user.setAccount(account);
        userRepository.save(user);
        return "redirect:/login?register_success";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            model.addAttribute("error", "Email không tồn tại!");
            return "forgot-password";
        }

        // Tạo mã ngẫu nhiên 6 chữ số
        String resetCode = String.format("%06d", new Random().nextInt(999999));
        resetCodes.put(email, resetCode); // Lưu mã tạm thời

        // Gửi email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Yêu cầu đặt lại mật khẩu - Vietjet Air");
        message.setText("Chào bạn,\n\n" +
                "Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. " +
                "Mã xác nhận của bạn là: " + resetCode + "\n" +
                "Vui lòng nhập mã này để tiếp tục.\n\n" +
                "Nếu bạn không yêu cầu, vui lòng bỏ qua email này.\n" +
                "Trân trọng,\nVietjet Air");
        mailSender.send(message);

        model.addAttribute("message", "Mã xác nhận đã được gửi! Vui lòng kiểm tra email.");
        model.addAttribute("email", email); // Truyền email để dùng ở bước tiếp theo
        return "reset-password"; // Chuyển sang trang nhập mã
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("email") String email,
                                       @RequestParam("code") String code,
                                       @RequestParam("newPassword") String newPassword,
                                       Model model) {
        String storedCode = resetCodes.get(email);
        if (storedCode == null || !storedCode.equals(code)) {
            model.addAttribute("error", "Mã xác nhận không đúng!");
            model.addAttribute("email", email);
            return "reset-password";
        }

        // Cập nhật mật khẩu mới
        Account account = accountService.getAccountByEmail(email);
        account.setPassword(passwordEncoder.encode(newPassword));
        accountService.save(account);
        resetCodes.remove(email); // Xóa mã sau khi dùng

        model.addAttribute("message", "Mật khẩu đã được đặt lại thành công!");
        return "redirect:/login?reset_success";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);
        User user = account.getUser();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(Model model, Authentication authentication) {
        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);
        User user = account.getUser();
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String processEditProfile(@ModelAttribute("user") @Valid User user,
                                     BindingResult result,
                                     Authentication authentication,
                                     Model model) {
        if (result.hasErrors()) {
            return "user/edit-profile";
        }
        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);
        User existingUser = account.getUser();
        existingUser.setFullName(user.getFullName());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        userRepository.save(existingUser);
        model.addAttribute("message", "Cập nhật hồ sơ thành công!");
        return "user/edit-profile";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "user/change-password";
    }

    @PostMapping("/profile/change-password")
    public String processChangePassword(@ModelAttribute("passwordForm") PasswordForm passwordForm,
                                        Authentication authentication,
                                        Model model) {
        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(passwordForm.getOldPassword(), account.getPassword())) {
            model.addAttribute("error", "Mật khẩu cũ không đúng!");
            return "user/change-password";
        }

        // Cập nhật mật khẩu mới
        account.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
        accountService.save(account);

        model.addAttribute("message", "Đổi mật khẩu thành công!");
        return "user/change-password";
    }

    // Class để xử lý form đổi mật khẩu
    public static class PasswordForm {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}