package com.codegym.airline_tickets.config;

import com.codegym.airline_tickets.security.CustomAuthenticationProvider;
import com.codegym.airline_tickets.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    // Thêm LoginAttemptService để tính phút còn lại khi khóa
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/login/google",
                                "/register", "/forgot-password", "/reset-password",
                                "/verify-registration",
                                "/user/css/**", "/user/js/**", "/user/plugins/**", "/user/img/**", "/user/icon/**",
                                "/admin/css/**", "/admin/js/**", "/admin/flight-seat/flight/**",
                                "/news", "/news/detail/**", "/policy/**", "/ws-chat/**", "/api/**", "/admin/**"
                        ).permitAll()
                        .requestMatchers("/profile/**", "/payment/**", "/booking/**", "/user/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureHandler(customFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(securityContextRepository())
                );
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler customFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                                                HttpServletResponse response,
                                                AuthenticationException exception) throws IOException, ServletException {
                // Lấy username (đang dùng làm khóa trong LoginAttemptService)
                String email = request.getParameter("username");

                if (exception instanceof LockedException) {
                    // Tài khoản bị khóa ⇒ tính số phút còn lại
                    long remaining = loginAttemptService.getRemainingLockTime(email);
                    // Lưu vào session để Thymeleaf có thể lấy ra
                    HttpSession session = request.getSession();
                    session.setAttribute("LOCK_REMAINING", remaining);
                    // Redirect về /login?error=locked
                    getRedirectStrategy().sendRedirect(request, response, "/login?error=locked");
                } else {
                    // Sai email hoặc mật khẩu ⇒ redirect về /login?error=true
                    getRedirectStrategy().sendRedirect(request, response, "/login?error=true");
                }
            }
        };
    }
}
