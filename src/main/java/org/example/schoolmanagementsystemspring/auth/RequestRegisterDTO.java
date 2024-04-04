package org.example.schoolmanagementsystemspring.auth;

import org.example.schoolmanagementsystemspring.user.Role;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public record RequestRegisterDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        MultipartFile picture,
        Role role
) {
}
