package org.example.schoolmanagementsystemspring.user.utils;

import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserResponseDto;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

public final class UserUtility {

    private UserUtility() {}


    public static UserResponseDto convertToDto(User user) {
        return UserResponseDto
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static User convertToEntity(UserRequestDto user, PasswordEncoder encoder) {
        return User
                .builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .password(encoder.encode(user.password()))
                .role(user.role())
                .isLocked(false)
                .isEnabled(true)
                .expirationDate(LocalDateTime.now().plusYears(1))
                .build();
    }
}
