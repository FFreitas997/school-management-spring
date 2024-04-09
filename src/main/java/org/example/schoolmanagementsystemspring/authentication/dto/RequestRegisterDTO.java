package org.example.schoolmanagementsystemspring.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.schoolmanagementsystemspring.user.entity.Role;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public record RequestRegisterDTO(

        @NotBlank(message = "First name mustn't be null or empty")
        @Size(min = 5, max = 100, message = "First name must be between 5 and 100 characters")
        String firstName,

        @NotBlank(message = "Last name mustn't be null or empty")
        @Size(min = 5, max = 100, message = "Last name must be between 5 and 100 characters")
        String lastName,

        @NotBlank(message = "Email mustn't be null or empty")
        @Size(min = 6, max = 255, message = "Email must be between 6 and 255 characters")
        @Email(message = "Email must be a valid email", regexp = "^(.+)@(.+)$")
        String email,

        @NotBlank(message = "Password mustn't be null or empty")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String password,

        @NotNull(message = "Role mustn't be null")
        Role role
) {
}
