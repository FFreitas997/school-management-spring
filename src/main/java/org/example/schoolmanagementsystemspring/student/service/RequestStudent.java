package org.example.schoolmanagementsystemspring.student.service;

import jakarta.validation.constraints.*;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.student.entity.Student}
 */
public record RequestStudent(

        @NotNull(message = "First name is required")
        @NotEmpty(message = "First name is required")
        @Length(min = 5, max = 20, message = "First name must be between 5 and 20 characters")
        String firstName,

        @NotNull(message = "Last name is required")
        @NotEmpty(message = "Last name is required")
        @Length(min = 5, max = 20, message = "Last name must be between 5 and 20 characters")
        String lastName,

        @NotNull(message = "Email is required")
        @Email(message = "Email should be valid", regexp = "^(.+)@(.+)$")
        @NotEmpty(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @NotEmpty(message = "Password is required")
        @Length(max = 8, message = "Password must be at most 8 characters")
        String password,

        @Length(max = 300, message = "Description must be at most 300 characters")
        String description,

        String studentIdentification,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDateTime birthDate,

        @NotNull(message = "Grade level is required")
        GradeLevel gradeLevel,

        @Length(max = 300, message = "Medical information must be at most 300 characters")
        String medicalInformation,

        @NotNull(message = "School ID is required")
        @Positive(message = "School ID must be positive")
        Integer schoolID

) implements Serializable {
}