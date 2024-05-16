package org.example.schoolmanagementsystemspring.school.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import org.example.schoolmanagementsystemspring.event.EventType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.event.Event}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RequestEvent(

        @NotBlank(message = "Title shouldn't be null or empty")
        @Max(value = 100, message = "Title must be less than 100 characters")
        String title,

        @NotBlank(message = "Description shouldn't be null or empty")
        @Max(value = 500, message = "Description must be less than 500 characters")
        String description,

        @NotNull(message = "Start date shouldn't be null")
        @Future(message = "Start date should be in the future")
        LocalDateTime start,

        @NotNull(message = "End date shouldn't be null")
        @Future(message = "End date should be in the future")
        LocalDateTime end,

        @NotNull(message = "Type shouldn't be null")
        EventType type,

        @NotNull(message = "School ID shouldn't be null")
        Integer schoolID

) implements Serializable {
}