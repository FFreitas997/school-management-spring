package org.example.schoolmanagementsystemspring.teacher.mappers;

import org.example.schoolmanagementsystemspring.teacher.dto.RequestTeacher;
import org.example.schoolmanagementsystemspring.teacher.entity.Teacher;
import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Component
public class TeacherMapper implements Function<RequestTeacher, Teacher>{

    @Override
    public Teacher apply(RequestTeacher request) {
        return Teacher
                .builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .role(Role.TEACHER)
                .description(request.description())
                .password(request.password())
                .department(request.department())
                .phoneNumber(request.phoneNumber())
                .educationQualification(request.educationQualifications())
                .experience(request.experience())
                .recognition(request.recognition())
                .teachMethod(request.teachMethod())
                .isEnabled(false)
                .build();
    }
}
