package org.example.schoolmanagementsystemspring.teacher.mappers;

import org.example.schoolmanagementsystemspring.teacher.entity.Teacher;
import org.example.schoolmanagementsystemspring.teacher.dto.CourseDTO;
import org.example.schoolmanagementsystemspring.teacher.dto.TeacherResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Component
public class TeacherResponseMapper implements Function<Teacher, TeacherResponse>{

    @Override
    public TeacherResponse apply(Teacher teacher) {

        return TeacherResponse
                .builder()
                .email(teacher.getEmail())
                .phoneNumber(teacher.getPhoneNumber())
                .department(teacher.getDepartment())
                .educationQualification(teacher.getEducationQualification())
                .experience(teacher.getExperience())
                .recognition(teacher.getRecognition())
                .teachMethod(teacher.getTeachMethod())
                .schoolName(teacher.getSchool().getName())
                .schoolID(teacher.getSchool().getId().toString())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .description(teacher.getDescription())
                .build();
    }
}
