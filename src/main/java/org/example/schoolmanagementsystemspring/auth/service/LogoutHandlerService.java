package org.example.schoolmanagementsystemspring.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.token.Token;
import org.example.schoolmanagementsystemspring.token.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Service
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final TokenRepository repository;

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);
        Token storedToken = repository
                .findByToken(token)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            storedToken.setExpiredAt(LocalDateTime.now());
            storedToken.setRevokedAt(LocalDateTime.now());
            repository.save(storedToken);
        }
    }
}
