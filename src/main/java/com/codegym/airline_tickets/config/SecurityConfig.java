package com.codegym.airline_tickets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new SessionFixationProtectionStrategy();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login","/login/google",
                                "/register",
                                "/forgot-password",
                                "/reset-password",
                                "/verify-registration",
                                "/user/css/**",
                                "/user/js/**",
                                "/user/plugins/**",
                                "/user/img/**",
                                "/user/icon/**",
                                "/admin/css/**",
                                "/admin/js/**",
                                "/admin/flight-seat/flight/**",
                                "/news",
                                "/news/detail/**",
                                "/policy/**",
                                "/ws-chat/**",
                                "/api/**",
                                "/admin/**"
                        ).permitAll()
//                        .requestMatchers("/admin/**").hasAnyRole("ADMIN,EMPLOYEE")
//                                .requestMatchers("/admin/news/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/profile/**", "/payment/**", "/booking/**", "/user/**", "/admin/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(securityContextRepository())
                );
        return http.build();
    }

}