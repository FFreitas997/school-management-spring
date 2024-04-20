package org.example.schoolmanagementsystemspring.user.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.user.entity.User}
 */
@Builder
public record UserDto(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String role,
        List<String> authorities
) implements Serializable { }