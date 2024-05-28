package org.example.schoolmanagementsystemspring.teacher.mappers;

import org.example.schoolmanagementsystemspring.student.entity.Student;
import org.example.schoolmanagementsystemspring.teacher.dto.StudentResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Component
public class StudentResponseMapper implements Function<Student, StudentResponse> {

    @Override
    public StudentResponse apply(Student student) {
        return StudentResponse
                .builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .description(student.getDescription())
                .studentIdentification(student.getStudentIdentification())
                .gradeLevel(student.getGradeLevel())
                .medicalInformation(student.getMedicalInformation())
                .birthDate(student.getBirthDate())
                .schoolName(student.getSchool().getName())
                .schoolID(student.getSchool().getId())
                .schoolName(student.getSchool().getName())
                .build();
    }
}
