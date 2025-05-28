package com.codegym.airline_tickets.eventListener;

import com.codegym.airline_tickets.entity.Log;
import com.codegym.airline_tickets.service.impl.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvent {

    @Autowired
    private LogService logService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        Log login = new Log();
        login.setUsername(username);
        login.setAction("Đăng nhập thành công");
        login.setIdAffected("N/A");
        login.setTimestamp(java.time.LocalDateTime.now());
        logService.save(login);
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        Log failure = new Log();
        failure.setUsername(username);
        failure.setAction("Đăng nhập thất bại");
        failure.setIdAffected("N/A");
        failure.setTimestamp(java.time.LocalDateTime.now());
        logService.save(failure);
    }

    @EventListener
    public void onLogout(LogoutSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            Log logout = new Log();
            logout.setUsername(username);
            logout.setAction("Đăng xuất thành công");
            logout.setIdAffected("N/A");
            logout.setTimestamp(java.time.LocalDateTime.now());
            logService.save(logout);
        }
    }
}
