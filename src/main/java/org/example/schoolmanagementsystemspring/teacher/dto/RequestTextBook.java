package org.example.schoolmanagementsystemspring.teacher.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.textbook.TextBook}
 */
public record RequestTextBook(

        @NotNull(message = "Title is required")
        @NotEmpty(message = "Title is required")
        @Length(max = 50, message = "Title must be less than 50 characters")
        String title,

        @Length(max = 300, message = "Description must be less than 300 characters")
        String description,

        @NotNull(message = "Author is required")
        String author,

        String edition,

        @NotNull(message = "ISBN is required")
        String isbn,

        @NotNull(message = "Cost is required")
        @Positive(message = "Cost must be greater than 0")
        Integer cost,

        @URL(protocol = "https", port = 80, message = "Link must be a valid URL")
        String link
) implements Serializable {
}