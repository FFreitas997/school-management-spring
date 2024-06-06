package org.example.schoolmanagementsystemspring.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.student.entity.Student}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResponseStudent(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String description,
        boolean isEnabled,
        String studentIdentification,
        LocalDateTime birthDate,
        GradeLevel gradeLevel,
        String medicalInformation
) implements Serializable { }