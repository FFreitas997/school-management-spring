package org.example.schoolmanagementsystemspring.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.teacher.entity.Teacher}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResponseTeacher(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String description,
        String department,
        String educationQualification,
        String experience,
        String recognition,
        String teachMethod,
        boolean enabled
) implements Serializable { }