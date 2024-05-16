package org.example.schoolmanagementsystemspring.school.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.school.entity.SchoolType;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SchoolResponse(
        Integer id,
        String name,
        SchoolType type,
        String email,
        String phoneNumber,
        String facebookURL,
        String instagramURL,
        String address,
        String city,
        String zipCode
) {
}
