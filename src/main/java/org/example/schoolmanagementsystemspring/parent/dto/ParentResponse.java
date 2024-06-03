package org.example.schoolmanagementsystemspring.parent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.parent.entity.ParentType;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.parent.entity.Parent}
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ParentResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String description,
        String phoneNumber,
        ParentType type,
        String occupation,
        StudentDto studentResponsible
) implements Serializable {

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.student.entity.Student}
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record StudentDto(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String description,
            String studentIdentification,
            LocalDateTime birthDate,
            GradeLevel gradeLevel,
            String medicalInformation
    ) implements Serializable {
    }
}