package org.example.schoolmanagementsystemspring.student.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TeacherResponse(
        String email,
        String phoneNumber,
        String department,
        String educationQualification,
        String experience,
        String recognition,
        String teachMethod,
        String firstName,
        String lastName,
        String description
) {
}
