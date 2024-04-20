package org.example.schoolmanagementsystemspring.mail;

import lombok.Builder;
import org.springframework.core.io.Resource;

import java.util.HashMap;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
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
