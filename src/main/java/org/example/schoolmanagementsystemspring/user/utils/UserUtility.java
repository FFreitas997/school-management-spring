package org.example.schoolmanagementsystemspring.user.utils;

import lombok.experimental.UtilityClass;
import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@UtilityClass
public class UserUtility {


    public UserDto convertToDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .authorities(user
                        .getAuthorities()
                        .stream()
                        .map(Object::toString)
                        .toList()
                )
                .build();
    }

    public User convertToEntity(UserRequestDto user, PasswordEncoder encoder) {
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
