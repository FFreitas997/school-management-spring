package org.example.schoolmanagementsystemspring.school.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.schoolmanagementsystemspring.school.entity.SchoolType;
import org.hibernate.validator.constraints.URL;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public record RequestSchool(

        @NotBlank(message = "School name shouldn't be null or empty")
        String name,

        @NotBlank(message = "School Type shouldn't be null or empty")
        SchoolType type,

        @NotBlank(message = "School Email shouldn't be null or empty")
        @Email(message = "Email must be a valid email", regexp = "^(.+)@(.+)$")
        String email,

        @NotBlank(message = "School Phone number shouldn't be null or empty")
        @Size(min = 9, max = 9, message = "Phone number must be 9 numbers")
        String phoneNumber,

        @URL(message = "URL must be a valid URL", host = "www.facebook.com", protocol = "https")
        String facebookURL,

        @URL(message = "URL must be a valid URL", host = "www.instagram.com", protocol = "https")
        String instagramURL,

        @NotBlank(message = "School Address shouldn't be null or empty")
        String address,

        String city,

        String zipCode
) {
}
