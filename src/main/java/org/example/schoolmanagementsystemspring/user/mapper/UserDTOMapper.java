package org.example.schoolmanagementsystemspring.user.mapper;

import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Component
public class UserDTOMapper implements Function<User, UserDto> {

    @Override
    public UserDto apply(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        return UserDto
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .authorities(
                        user
                                .getAuthorities()
                                .stream()
                                .map(Object::toString)
                                .toList()
                )
                .build();
    }
}
