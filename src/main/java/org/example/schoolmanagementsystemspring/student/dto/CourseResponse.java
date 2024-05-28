package org.example.schoolmanagementsystemspring.student.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.student.entity.GradeLevel;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.course.Course}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseResponse(
        Integer id,
        String courseName,
        String courseDescription,
        String preRequisites,
        String courseCode,
        GradeLevel gradeLevel,
        List<TextBookDto> textBooks
) implements Serializable {

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.textbook.TextBook}
     */
    public record TextBookDto(
            Integer id,
            String title,
            String description,
            String author,
            String link
    ) implements Serializable { }
}