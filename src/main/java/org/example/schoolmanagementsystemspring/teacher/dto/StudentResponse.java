package org.example.schoolmanagementsystemspring.teacher.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.student.GradeLevel;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */


@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record StudentResponse(
        String firstName,
        String lastName,
        String email,
        String description,
        String studentIdentification,
        GradeLevel gradeLevel,
        String medicalInformation,
        LocalDateTime birthDate,
        String schoolName,
        Integer schoolID
) { }
