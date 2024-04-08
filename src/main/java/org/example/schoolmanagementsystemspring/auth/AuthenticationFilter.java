package org.example.schoolmanagementsystemspring.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.auth.service.JwtService;
import org.example.schoolmanagementsystemspring.exception.GlobalHandlerException;
import org.example.schoolmanagementsystemspring.token.Token;
import org.example.schoolmanagementsystemspring.token.TokenRepository;
import org.example.schoolmanagementsystemspring.user.User;
import org.example.schoolmanagementsystemspring.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            String authHeader = req.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                chain.doFilter(req, res);
                return;
            }
            String token = authHeader.substring(7);
            String usernameToken = jwtService.getSubject(token);
            if (usernameToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository
                        .findByUsername(usernameToken)
                        .orElse(null);
                Token tokenEntity = tokenRepository
                        .findByTokenValid(token)
                        .orElse(null);
                if (user != null && tokenEntity != null && jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            chain.doFilter(req, res);
        } catch (JwtException e) {
            res.setStatus(HttpStatus.FORBIDDEN.value());
            res.getWriter().write(e.getMessage());
        }
    }
}
