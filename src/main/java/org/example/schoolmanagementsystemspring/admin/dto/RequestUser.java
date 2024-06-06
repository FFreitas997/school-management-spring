package org.example.schoolmanagementsystemspring.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.user.entity.User}
 */
public record RequestUser(

        @NotNull(message = "First name shouldn't be null")
        @NotEmpty(message = "First name shouldn't be empty")
        @Length(min = 5, max = 100, message = "First name must be between 5 and 100 characters")
        String firstName,

        @NotNull(message = "Last name shouldn't be null")
        @NotEmpty(message = "Last name shouldn't be empty")
        @Length(min = 5, max = 100, message = "Last name must be between 5 and 100 characters")
        String lastName,

        @NotNull(message = "Email shouldn't be null")
        @Email(message = "Email must be a valid email", regexp = "^(.+)@(.+)$")
        @NotEmpty(message = "Email shouldn't be empty")
        String email,

        @NotNull(message = "Password shouldn't be null")
        @NotEmpty(message = "Password shouldn't be empty")
        @Length(min = 8, max = 255, message = "Password should be between 8 and 255 characters")
        String password,

        @NotNull(message = "Role shouldn't be null")
        Role role,

        @NotNull(message = "Description shouldn't be null")
        @NotEmpty(message = "Description shouldn't be empty")
        @Length(max = 500, message = "Description must be less than 500 characters")
        String description

) implements Serializable {
}