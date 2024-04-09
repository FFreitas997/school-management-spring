package org.example.schoolmanagementsystemspring.exception;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Builder
public record ErrorValidation(
        String message,
        Integer status,
        LocalDateTime current,
        Map<String, String> fields
) {
}
