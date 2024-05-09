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
 * The LogoutHandlerService class is a service that handles the logout process.
 * It implements the LogoutHandler interface, which provides a contract for handling logout.
 * It uses the TokenRepository to perform its operations.
 * It checks the Authorization header of the request, and if it starts with "Bearer", it attempts to handle the token.
 * If the Authorization header is missing or does not start with "Bearer", it returns without doing anything.
 *
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

    /**
     * The logout method is overridden to handle the logout process.
     * It first checks the Authorization header of the request.
     * If the header is missing or does not start with "Bearer", it returns without doing anything.
     * Otherwise, it retrieves the token from the header and attempts to handle it.
     *
     * @param req  the HttpServletRequest object containing the details of the request.
     * @param res  the HttpServletResponse object for sending the response.
     * @param auth the Authentication object containing the details of the authentication.
     */
    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
        String authHeader = req.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TokenType.BEARER.getValue())) {
            log.warn("Logout: Request without Authorization header ...");
            return;
        }
        String token = authHeader.substring(7);
        repository.findByToken(token)
                .ifPresent(this::handleToken);
    }

    /**
     * This method handles a token during the logout process.
     * It sets the token as expired and saves it in the repository.
     *
     * @param token the token to be handled.
     */
    private void handleToken(Token token) {
        token.setExpired(true);
        repository.save(token);
    }
}