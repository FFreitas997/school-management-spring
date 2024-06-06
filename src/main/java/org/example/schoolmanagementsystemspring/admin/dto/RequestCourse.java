package org.example.schoolmanagementsystemspring.admin.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.course.Course}
 */
public record RequestCourse(

        @NotNull(message = "Course name shouldn't be null")
        @NotEmpty(message = "Course name shouldn't be empty")
        @Length(min = 5, max = 100, message = "Course name must be between 5 and 100 characters")
        String courseName,

        @NotNull(message = "Course description shouldn't be null")
        @NotEmpty(message = "Course description shouldn't be empty")
        @Length(min = 5, max = 500, message = "Course description must be between 5 and 500 characters")
        String courseDescription,

        String preRequisites,

        String room,

        @NotNull(message = "Course code shouldn't be null")
        @NotEmpty(message = "Course code shouldn't be empty")
        @Length(min = 5, max = 100, message = "Course code must be between 5 and 100 characters")
        String courseCode,

        @NotNull(message = "Grade level shouldn't be null")
        GradeLevel gradeLevel

) implements Serializable {
}