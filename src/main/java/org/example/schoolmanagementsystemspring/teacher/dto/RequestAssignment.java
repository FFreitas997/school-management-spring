package org.example.schoolmanagementsystemspring.teacher.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.schoolmanagementsystemspring.assignment.AssignmentType;
import org.example.schoolmanagementsystemspring.assignment.SubmissionType;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public record RequestAssignment(

        @NotBlank(message = "Course code shouldn't be null or empty")
        String courseCode,

        @NotNull(message = "Deliver date shouldn't be null")
        @Future(message = "Deliver date must be in the future")
        LocalDateTime deliverAssignment,

        @NotBlank(message = "Title shouldn't be null or empty")
        @Length(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
        String title,

        @NotBlank(message = "Description shouldn't be null or empty")
        @Length(max = 600, message = "Description must be less than 600 characters")
        String description,

        @NotNull(message = "Points shouldn't be null")
        Integer points,

        @NotNull(message = "Submission type shouldn't be null")
        SubmissionType submissionType,

        @NotNull(message = "Assignment type shouldn't be null")
        AssignmentType assignmentType
) {
}
