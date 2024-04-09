package org.example.schoolmanagementsystemspring.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponseDto;
import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.dto.ResponseRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.repository.TokenRepository;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.example.schoolmanagementsystemspring.authentication.utils.AuthUtility;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    @Override
    public ResponseRegisterDTO register(RequestRegisterDTO requestBody) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = AuthUtility.mapToUser(requestBody, encoder, auth);
        User savedUser = userRepository.save(user);
        String generatedToken = jwtService.generateToken(savedUser, Collections.emptyMap());
        Token token = AuthUtility.mapToToken(savedUser, generatedToken);
        Token savedToken = tokenRepository.save(token);
        String generatedRefreshToken = jwtService.generateRefreshToken(savedUser);
        return new ResponseRegisterDTO(savedToken.getTokenValue(), generatedRefreshToken);
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto requestBody) {
        User user = userRepository
                .findByEmail(requestBody.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String generatedToken = jwtService.generateToken(user, Collections.emptyMap());
        String generatedRefreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        Token token = AuthUtility.mapToToken(user, generatedToken);
        tokenRepository.save(token);
        manager.authenticate(new UsernamePasswordAuthenticationToken(requestBody.email(), requestBody.password()));
        return new AuthenticationResponseDto(generatedToken, generatedRefreshToken);
    }

    @Override
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String refreshToken = authHeader.substring(7);
        String username = jwtService.getSubject(refreshToken);
        if (username != null) {
            User user = userRepository.findByEmail(username).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user, Collections.emptyMap());
                revokeAllUserTokens(user);
                Token token = AuthUtility.mapToToken(user, accessToken);
                tokenRepository.save(token);
                AuthenticationResponseDto authResponse = new AuthenticationResponseDto(accessToken, refreshToken);
                new ObjectMapper().writeValue(res.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository
                .findAllValidTokensByUserId(user.getId())
                .forEach(token -> {
                    token.setRevoked(true);
                    token.setExpired(true);
                });
    }
}
