package org.example.schoolmanagementsystemspring.user.mapper;

import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
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
