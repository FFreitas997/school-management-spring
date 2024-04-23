package org.example.schoolmanagementsystemspring.authentication.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.example.schoolmanagementsystemspring.authentication.repository.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final TokenRepository repository;

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
        String authHeader = req.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TokenType.BEARER.getValue())) {
            log.warn("Logout: Request without Authorization header ...");
            return;
        }
        String token = authHeader.substring(7);
        repository
                .findByToken(token)
                .ifPresent(this::handleToken);
    }

    private void handleToken(Token token) {
        token.setExpired(true);
        repository.save(token);
    }
}
