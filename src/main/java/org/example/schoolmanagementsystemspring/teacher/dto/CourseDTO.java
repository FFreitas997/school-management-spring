package org.example.schoolmanagementsystemspring.teacher.helpers;

import org.example.schoolmanagementsystemspring.student.GradeLevel;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public record CourseDTO(
        Integer id,
        String name,
        String code,
        GradeLevel level
) {
}
