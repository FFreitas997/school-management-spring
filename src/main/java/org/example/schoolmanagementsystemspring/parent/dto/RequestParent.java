package org.example.schoolmanagementsystemspring.parent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.schoolmanagementsystemspring.parent.entity.ParentType;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.parent.entity.Parent}
 */
public record RequestParent(

        @NotNull(message = "First name cannot be null")
        @NotEmpty(message = "First name cannot be empty")
        @Length(min = 5, max = 20, message = "First name must be between 5 and 20 characters")
        String firstName,

        @NotNull(message = "Last name cannot be null")
        @NotEmpty(message = "Last name cannot be empty")
        @Length(min = 5, max = 20, message = "Last name must be between 5 and 20 characters")
        String lastName,

        @NotNull(message = "Email cannot be null")
        @Email(message = "Email must be a valid email", regexp = "^(.+)@(.+)$")
        @NotEmpty(message = "Email cannot be empty")
        String email,

        @NotNull(message = "Password cannot be null")
        @NotEmpty(message = "Password cannot be empty")
        @Length(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String password,

        @Length(max = 500, message = "Description must be less than 500 characters")
        String description,

        @NotNull(message = "Phone number cannot be null")
        @Size(min = 9, max = 9, message = "Phone number must be 9 numbers")
        @NotEmpty(message = "Phone number cannot be empty")
        String phoneNumber,

        ParentType type,

        @NotNull(message = "Occupation cannot be null")
        @NotEmpty(message = "Occupation cannot be empty")
        String occupation

) implements Serializable {
}