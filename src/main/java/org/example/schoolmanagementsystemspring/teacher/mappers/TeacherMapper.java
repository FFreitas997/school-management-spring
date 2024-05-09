package org.example.schoolmanagementsystemspring.teacher;

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
public class TeacherMapper implements Function<RegisterTeacherDTO, Teacher>{

    @Override
    public Teacher apply(RegisterTeacherDTO request) {
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
                .expirationDate(LocalDateTime.now().plusYears(1))
                .notification("Account has been created, needs to be activated")
                .isEnabled(false)
                .isLocked(false)
                .build();
    }
}
