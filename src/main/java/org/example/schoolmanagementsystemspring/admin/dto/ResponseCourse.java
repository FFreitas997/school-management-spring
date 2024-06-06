package org.example.schoolmanagementsystemspring.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.school.entity.SchoolType;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.course.Course}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResponseCourse(
        Integer id,
        String courseName,
        String courseDescription,
        String preRequisites,
        String room,
        String courseCode,
        GradeLevel gradeLevel,
        TeacherDto teacher,
        SchoolDto school
) implements Serializable {

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.teacher.entity.Teacher}
     */

    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record TeacherDto(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String department
    ) implements Serializable {
    }

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.school.entity.School}
     */

    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record SchoolDto(
            String email,
            String zipCode,
            Integer id,
            String name,
            SchoolType schoolType
    ) implements Serializable {
    }
}