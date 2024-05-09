package org.example.schoolmanagementsystemspring.teacher.mappers;

import org.example.schoolmanagementsystemspring.course.Course;
import org.example.schoolmanagementsystemspring.teacher.dto.CourseResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Component
public class CourseResponseMapper implements Function<Course, CourseResponse> {

    @Override
    public CourseResponse apply(Course course) {

        var textBooks = course.getTextBooks()
                .stream()
                .map(param -> new CourseResponse.TextBookDto(param.getTitle(), param.getDescription(), param.getAuthor()))
                .toList();

        return CourseResponse.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .courseDescription(course.getCourseDescription())
                .courseCode(course.getCourseCode())
                .preRequisites(course.getPreRequisites())
                .gradeLevel(course.getGradeLevel())
                .schoolId(course.getSchool().getId())
                .schoolName(course.getSchool().getName())
                .textBooks(textBooks)
                .build();
    }
}
