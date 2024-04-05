package org.example.schoolmanagementsystemspring.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.auth.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.auth.dto.AuthenticationResponseDto;
import org.example.schoolmanagementsystemspring.auth.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.auth.dto.ResponseRegisterDTO;
import org.example.schoolmanagementsystemspring.token.Token;
import org.example.schoolmanagementsystemspring.token.TokenRepository;
import org.example.schoolmanagementsystemspring.token.TokenType;
import org.example.schoolmanagementsystemspring.user.User;
import org.example.schoolmanagementsystemspring.user.UserRepository;
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
        User user = mapToUser(requestBody);
        User savedUser = userRepository.save(user);
        String generatedToken = jwtService.generateToken(savedUser, Collections.emptyMap());
        Token token = mapToToken(savedUser, generatedToken);
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
        Token token = mapToToken(user, generatedToken);
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
                Token token = mapToToken(user, accessToken);
                tokenRepository.save(token);
                AuthenticationResponseDto authResponse = new AuthenticationResponseDto(accessToken, refreshToken);
                new ObjectMapper().writeValue(res.getOutputStream(), authResponse);
            }
        }
    }

    private User mapToUser(RequestRegisterDTO dto) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return User
                .builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(encoder.encode(dto.password()))
                .role(dto.role())
                .expirationDate(LocalDateTime.now().plusYears(1))
                .isEnabled(true)
                .isLocked(false)
                .createdAt(LocalDateTime.now())
                .createdBy(authentication.getName())
                .imageName(dto.picture().getOriginalFilename())
                .imageType(dto.picture().getContentType())
                .imageData(dto.picture().getBytes())
                .build();
    }

    private Token mapToToken(User user, String token) {
        return Token
                .builder()
                .tokenValue(token)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.findAllValidTokensByUserId(user.getId()).forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
            token.setRevokedAt(LocalDateTime.now());
            token.setExpiredAt(LocalDateTime.now());
        });
    }
}
