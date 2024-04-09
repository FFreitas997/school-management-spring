package org.example.schoolmanagementsystemspring.user.entity;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Getter
public enum Role {
    ADMIN(
            Permission.ADMIN_CREATE, Permission.ADMIN_READ,
            Permission.ADMIN_UPDATE, Permission.ADMIN_DELETE,
            Permission.TEACHER_CREATE, Permission.TEACHER_READ,
            Permission.TEACHER_UPDATE, Permission.TEACHER_DELETE,
            Permission.STUDENT_CREATE, Permission.STUDENT_READ,
            Permission.STUDENT_UPDATE, Permission.STUDENT_DELETE,
            Permission.PARENT_CREATE, Permission.PARENT_READ,
            Permission.PARENT_UPDATE, Permission.PARENT_DELETE
    ),
    TEACHER(
            Permission.TEACHER_CREATE, Permission.TEACHER_READ,
            Permission.TEACHER_UPDATE, Permission.TEACHER_DELETE
    ),
    STUDENT(
            Permission.STUDENT_CREATE, Permission.STUDENT_READ,
            Permission.STUDENT_UPDATE, Permission.STUDENT_DELETE
    ),
    PARENT(
            Permission.PARENT_CREATE, Permission.PARENT_READ,
            Permission.PARENT_UPDATE, Permission.PARENT_DELETE
    );

    private final List<Permission> permissions;

    Role(Permission... permissions) { this.permissions = List.of(permissions); }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = permissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
