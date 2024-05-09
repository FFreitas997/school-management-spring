package org.example.schoolmanagementsystemspring.teacher.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.student.GradeLevel;

import java.io.Serializable;
import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseResponse(
        String courseName,
        String courseDescription,
        String courseCode,
        String preRequisites,
        GradeLevel gradeLevel,
        Integer id,
        Integer schoolId,
        String schoolName,
        List<TextBookDto> textBooks
) {

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.textbook.TextBook}
     */

    public record TextBookDto(
            String title,
            String description,
            String author
    ) implements Serializable { }
}
