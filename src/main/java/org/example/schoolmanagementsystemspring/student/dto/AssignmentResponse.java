package org.example.schoolmanagementsystemspring.student.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.assignment.AssignmentType;
import org.example.schoolmanagementsystemspring.assignment.SubmissionType;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AssignmentResponse(
        Integer studentID,
        String courseCode,
        LocalDateTime delivery,
        String title,
        String description,
        SubmissionType submissionType,
        AssignmentType assignmentType,
        Double grade,
        String feedback
) { }
