package org.example.schoolmanagementsystemspring.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@RequiredArgsConstructor
@Getter
public enum Permission {
    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    TEACHER_CREATE("teacher:create"),
    TEACHER_READ("teacher:read"),
    TEACHER_UPDATE("teacher:update"),
    TEACHER_DELETE("teacher:delete"),
    STUDENT_CREATE("student:create"),
    STUDENT_READ("student:read"),
    STUDENT_UPDATE("student:update"),
    STUDENT_DELETE("student:delete"),
    PARENT_CREATE("parent:create"),
    PARENT_READ("parent:read"),
    PARENT_UPDATE("parent:update"),
    PARENT_DELETE("parent:delete");

    private final String value;
}
