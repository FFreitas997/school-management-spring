package org.example.schoolmanagementsystemspring.student.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.parent.entity.ParentType;
import org.example.schoolmanagementsystemspring.parent.entity.Parent;

import java.io.Serializable;

/**
 * DTO for {@link Parent}
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ParentResponse(
        String firstName,
        String lastName,
        String email,
        String description,
        String phoneNumber,
        ParentType type,
        String occupation
) implements Serializable { }