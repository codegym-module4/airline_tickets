package com.codegym.airline_tickets.security;

import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountService accountService; // Dùng để load Account và UserDetails cũ

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        // --- Bước 1: Kiểm tra xem email có đang bị khóa không ---
        if (loginAttemptService.isBlocked(email)) {
            long remaining = loginAttemptService.getRemainingLockTime(email);
            throw new LockedException("Tài khoản đã bị khóa. Vui lòng thử lại sau " + remaining + " phút.");
        }

        // --- Bước 2: Load Account từ database (qua AccountService) ---
        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            // Email không tồn tại → tính là login thất bại
            loginAttemptService.loginFailed(email);
            throw new BadCredentialsException("Email hoặc mật khẩu không đúng.");
        }

        // --- Bước 3: Kiểm tra mật khẩu ---
        if (!passwordEncoder.matches(rawPassword, account.getPassword())) {
            // Sai mật khẩu → tăng count; nếu đủ 3 lần thì được khóa
            loginAttemptService.loginFailed(email);
            int attempts = loginAttemptService.getFailedAttempts(email); // gọi getter thay vì truy cập field
            if (attempts >= LoginAttemptService.MAX_FAILED_ATTEMPTS) {
                throw new LockedException("Bạn đã thử quá nhiều lần. Tài khoản bị khóa 15 phút.");
            }
            throw new BadCredentialsException("Email hoặc mật khẩu không đúng.");
        }

        // --- Bước 4: Đăng nhập thành công → reset count và tạo Authentication ---
        loginAttemptService.loginSucceeded(email);
        UserDetails userDetails = accountService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, rawPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
