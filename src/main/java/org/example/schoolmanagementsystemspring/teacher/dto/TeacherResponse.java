package org.example.schoolmanagementsystemspring.teacher.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

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
        String schoolName,
        String schoolID,
        String firstName,
        String lastName,
        String description
) { }