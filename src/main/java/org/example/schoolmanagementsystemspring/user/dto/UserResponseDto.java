package org.example.schoolmanagementsystemspring.user.dto;

import lombok.Builder;
import org.example.schoolmanagementsystemspring.user.entity.Role;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.user.entity.User}
 */
@Builder
public record UserResponseDto(
        String firstName,
        String lastName,
        String email,
        Role role
) implements Serializable { }