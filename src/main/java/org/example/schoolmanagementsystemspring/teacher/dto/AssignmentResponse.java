package org.example.schoolmanagementsystemspring.teacher.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.assignment.AssignmentType;
import org.example.schoolmanagementsystemspring.assignment.SubmissionType;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AssignmentResponse(
        Integer studentID,
        String courseCode,
        LocalDateTime delivery,
        String title,
        String description,
        Integer points,
        SubmissionType submissionType,
        AssignmentType assignmentType,
        Integer grade,
        String feedback
) {
}
