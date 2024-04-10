package org.example.schoolmanagementsystemspring.authentication.utils;

import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public final class AuthUtility {

    private AuthUtility() {
    }

    public static User mapToUser(RequestRegisterDTO dto, PasswordEncoder encoder, Authentication authentication) {
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
                .createdBy("System")
                .build();
    }

    public static Token mapToToken(User user, String token) {
        return Token
                .builder()
                .tokenValue(token)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }
}
