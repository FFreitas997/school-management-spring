package org.example.schoolmanagementsystemspring.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.school.entity.SchoolType;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.school.entity.School}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record SchoolResponse(
        String email,
        String facebookURL,
        String instagramURL,
        String address,
        String zipCode,
        Integer id,
        String name,
        SchoolType schoolType
) implements Serializable { }