package org.example.schoolmanagementsystemspring.admin.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.schoolmanagementsystemspring.event.EventType;

import java.time.LocalDateTime;

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

) {
}
