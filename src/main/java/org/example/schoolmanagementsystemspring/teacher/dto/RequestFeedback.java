package org.example.schoolmanagementsystemspring.teacher.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

public record RequestFeedback(

        @NotBlank(message = "Feedback shouldn't be null or empty")
        String feedback,

        @NotNull(message = "Grade shouldn't be null")
        @PositiveOrZero(message = "Grade must be positive or zero")
        @Size(min = 0, max = 20, message = "Grade must be between 0 and 20")
        Double grade,

        @NotBlank(message = "Course code shouldn't be null or empty")
        String courseCode,

        @NotNull(message = "Delivery date shouldn't be null")
        @Future(message = "Delivery date must be in the future")
        LocalDateTime delivery,

        @NotNull(message = "Student ID shouldn't be null")
        Integer studentID
) { }
