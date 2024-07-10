package org.example.schoolmanagementsystemspring.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.user.entity.Role;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.user.entity.User}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AdminResponse(
        String firstName,
        String lastName,
        String email,
        String description
) implements Serializable { }