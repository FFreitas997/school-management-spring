package org.example.schoolmanagementsystemspring.teacher.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

public record RequestTeacher(

        @NotBlank(message = "First name shouldn't be null or empty")
        @Size(min = 5, max = 100, message = "First name must be between 5 and 100 characters")
        String firstName,

        @NotBlank(message = "Last name shouldn't be null or empty")
        @Size(min = 5, max = 100, message = "Last name must be between 5 and 100 characters")
        String lastName,

        @NotBlank(message = "Email shouldn't be null or empty")
        @Size(min = 6, max = 255, message = "Email must be between 6 and 255 characters")
        @Email(message = "Email must be a valid email", regexp = "^(.+)@(.+)$")
        String email,

        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,

        @NotBlank(message = "Password shouldn't be null or empty")
        @Size(min = 8, max = 255, message = "Password should be between 8 and 255 characters")
        String password,

        @NotBlank(message = "Department shouldn't be null or empty")
        String department,

        @NotBlank(message = "Phone number shouldn't be null or empty")
        @Size(min = 9, max = 9, message = "Phone number must be 9 numbers")
        String phoneNumber,

        @Size(max = 300, message = "Education qualifications must be less than 300 characters")
        String educationQualifications,

        @Size(max = 300, message = "Experience must be less than 300 characters")
        String experience,

        @Size(max = 300, message = "Research interests must be less than 300 characters")
        String recognition,

        String teachMethod,

        @NotNull(message = "School ID shouldn't be null")
        Integer schoolID
) {
}
