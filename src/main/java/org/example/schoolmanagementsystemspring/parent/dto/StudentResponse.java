package org.example.schoolmanagementsystemspring.parent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.student.entity.Student}
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record StudentResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String description,
        String studentIdentification,
        LocalDateTime birthDate,
        GradeLevel gradeLevel,
        String medicalInformation,
        TeacherDto teacherResponsible,
        SchoolDto school,
        List<CourseDto> courses
) implements Serializable {

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.teacher.entity.Teacher}
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record TeacherDto(
            String firstName,
            String lastName,
            String email,
            String description,
            String phoneNumber,
            String department,
            String educationQualification,
            String experience,
            String recognition,
            String teachMethod
    ) implements Serializable { }

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.school.entity.School}
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record SchoolDto(
            String email,
            String phoneNumber,
            String facebookURL,
            String instagramURL,
            String address,
            String city,
            String zipCode,
            String name
    ) implements Serializable { }

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.course.Course}
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record CourseDto(
            String courseName,
            String courseDescription,
            String preRequisites,
            String room,
            String courseCode,
            GradeLevel gradeLevel
    ) implements Serializable { }
}