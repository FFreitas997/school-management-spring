package org.example.schoolmanagementsystemspring.student.mappers;

import org.example.schoolmanagementsystemspring.student.entity.Student;
import org.example.schoolmanagementsystemspring.student.service.RequestStudent;
import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StudentMapper implements Function<RequestStudent, Student> {


    @Override
    public Student apply(RequestStudent requestStudent) {
        return Student
                .builder()
                .firstName(requestStudent.firstName())
                .lastName(requestStudent.lastName())
                .email(requestStudent.email())
                .role(Role.STUDENT)
                .description(requestStudent.description())
                .password(requestStudent.password())
                .studentIdentification(requestStudent.studentIdentification())
                .birthDate(requestStudent.birthDate())
                .gradeLevel(requestStudent.gradeLevel())
                .medicalInformation(requestStudent.medicalInformation())
                .isEnabled(false)
                .build();
    }
}
