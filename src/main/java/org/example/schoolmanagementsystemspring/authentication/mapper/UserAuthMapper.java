package org.example.schoolmanagementsystemspring.authentication.mapper;

import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Component
public class UserAuthMapper implements BiFunction<RequestRegisterDTO, PasswordEncoder, User> {
    @Override
    public User apply(RequestRegisterDTO dto, PasswordEncoder passwordEncoder) {
        return User
                .builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(dto.role())
                .expirationDate(LocalDateTime.now().plusYears(1))
                .isEnabled(false)
                .isLocked(false)
                .notification(String.format("The user %s %s should confirm the email to enable the account.", dto.firstName(), dto.lastName()))
                .build();
    }
}
