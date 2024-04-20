package org.example.schoolmanagementsystemspring.mail;

import lombok.Builder;
import org.springframework.core.io.Resource;

import java.util.HashMap;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Builder
public record Email(
        String from,
        String to,
        String subject,
        EmailTemplate template,
        HashMap<String, Object> templateProperties,
        HashMap<String, Resource> resources
) {
}
