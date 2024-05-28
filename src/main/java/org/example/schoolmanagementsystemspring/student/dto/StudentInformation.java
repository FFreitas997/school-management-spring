package org.example.schoolmanagementsystemspring.student.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.student.entity.Student}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record StudentInformation(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String description,
        String studentIdentification,
        GradeLevel gradeLevel,
        String medicalInformation
) implements Serializable {
}