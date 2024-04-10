package org.example.schoolmanagementsystemspring.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Builder
public record ErrorCustomResponse(
        String message,
        HttpStatus status,
        LocalDateTime timestamp,
        String path,
        String error

) {
}
