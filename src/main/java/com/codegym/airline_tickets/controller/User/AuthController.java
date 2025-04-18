package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import com.codegym.airline_tickets.repository.UserRepository;
import com.codegym.airline_tickets.service.impl.FirebaseStorageService;
import com.codegym.airline_tickets.service.impl.AccountService;
import com.codegym.airline_tickets.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    private final Map<String, String> resetCodes = new HashMap<>();

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        Account account = new Account();
        account.setUser(new User());
        model.addAttribute("account", account);
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("account") @Valid Account account,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        if (accountService.getAccountByEmail(account.getEmail()) != null) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "auth/register";
        }
        accountService.register(account);
        return "redirect:/login?register_success";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            model.addAttribute("error", "Email không tồn tại!");
            return "auth/forgot-password";
        }

        String resetCode = String.format("%06d", new Random().nextInt(999999));
        resetCodes.put(email, resetCode);

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
        model.addAttribute("email", email);
        return "auth/reset-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "auth/reset-password";
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
            return "auth/reset-password";
        }

        Account account = accountService.getAccountByEmail(email);
        account.setPassword(passwordEncoder.encode(newPassword));
        accountService.save(account);
        resetCodes.remove(email);

        model.addAttribute("message", "Mật khẩu đã được đặt lại thành công!");
        return "redirect:/login?reset_success";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);
        User user = account.getUser();
        model.addAttribute("user", user);
        return "user/profile/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(Model model, Authentication authentication) {
        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);
        User user = account.getUser();
        model.addAttribute("user", user);
        return "user/profile/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String processEditProfile(@ModelAttribute("user") @Valid User user,
                                     @RequestParam("dob") String dobString,
                                     @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                     BindingResult result,
                                     Authentication authentication,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Dữ liệu không hợp lệ!");
            return "user/profile/edit-profile";
        }

        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);
        User existingUser = account.getUser();

        existingUser.setFullName(user.getFullName());
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            existingUser.setDob(LocalDate.parse(dobString, formatter));
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Ngày sinh không đúng định dạng (dd/MM/yyyy)!");
            return "user/profile/edit-profile";
        }
        existingUser.setAddress(user.getAddress());
        existingUser.setGender(user.getGender());
        existingUser.setPhone(user.getPhone());
        existingUser.setCitizenIdentification(user.getCitizenIdentification());
        existingUser.setNationality(user.getNationality());

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String oldImageUrl = existingUser.getImage();
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    firebaseStorageService.deleteFileFromUrl(oldImageUrl);
                }

                String imageUrl = firebaseStorageService.uploadFile(profileImage, existingUser.getId().toString());
                existingUser.setImage(imageUrl);
            } catch (IOException e) {
                model.addAttribute("error", "Không thể upload ảnh!");
                return "user/profile/edit-profile";
            }
        }

        userRepository.save(existingUser);

        UserDetails updatedUserDetails = accountService.loadUserByUsername(email);
        Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                updatedUserDetails, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        model.addAttribute("message", "Cập nhật hồ sơ thành công!");
        return "redirect:/profile";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "user/profile/change-password";
    }

    @PostMapping("/profile/change-password")
    public String processChangePassword(@ModelAttribute("passwordForm") PasswordForm passwordForm,
                                        Authentication authentication,
                                        Model model) {
        String email = authentication.getName();
        Account account = accountService.getAccountByEmail(email);

        if (!passwordEncoder.matches(passwordForm.getOldPassword(), account.getPassword())) {
            model.addAttribute("error", "Mật khẩu cũ không đúng!");
            return "user/profile/change-password";
        }

        account.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
        accountService.save(account);

        model.addAttribute("message", "Đổi mật khẩu thành công!");
        return "user/profile/change-password";
    }

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