package org.example.schoolmanagementsystemspring.authentication.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.example.schoolmanagementsystemspring.authentication.repository.TokenRepository;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The AuthenticationFilterService class is a filter that intercepts each request once and performs authentication.
 * It extends OncePerRequestFilter, which ensures a single execution per request dispatch.
 * It uses the JwtService, UserRepository, and TokenRepository to perform its operations.
 * It checks the Authorization header of the request, and if it starts with "Bearer", it attempts to authenticate the request.
 * If the request is already authenticated, or if the Authorization header is missing or does not start with "Bearer", it forwards the request without modifying it.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationFilterService extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    /**
     * The doFilterInternal method is overridden to perform the authentication.
     * It first checks the Authorization header of the request.
     * If the header is missing or does not start with "Bearer", it forwards the request without modifying it.
     * If the request is already authenticated, it also forwards the request without modifying it.
     * Otherwise, it retrieves the token from the header, gets the subject from the token, retrieves the user and the token entity from the repositories, and checks if the token is valid.
     * If the token is valid, it creates a new UsernamePasswordAuthenticationToken, sets its details, and sets it in the SecurityContext.
     * Finally, it forwards the request.
     *
     * @param req   the HttpServletRequest object containing the details of the request.
     * @param res   the HttpServletResponse object for sending the response.
     * @param chain the FilterChain object for forwarding the request.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an input or output error occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain) throws ServletException, IOException {
        log.info("Filtering request to authenticate ...");
        String authHeader = req.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TokenType.BEARER.getValue())) {
            log.warn("Request Filter: Request without Authorization header ...");
            chain.doFilter(req, res);
            return;
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.warn("Request already authenticated ...");
            chain.doFilter(req, res);
            return;
        }
        String token = authHeader.substring(7);
        String subject = jwtService.getSubject(token);
        User user = userRepository
                .findByEmailValid(subject)
                .orElse(null);
        Token tokenEntity = tokenRepository
                .findByTokenValid(token)
                .orElse(null);
        if (jwtService.isTokenValid(tokenEntity, user)) {
            assert user != null;
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        chain.doFilter(req, res);
    }
}