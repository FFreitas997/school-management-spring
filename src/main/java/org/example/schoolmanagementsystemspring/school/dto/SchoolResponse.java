package org.example.schoolmanagementsystemspring.school.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.school.entity.SchoolType;

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
) { }
