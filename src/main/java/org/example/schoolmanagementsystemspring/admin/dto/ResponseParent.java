package org.example.schoolmanagementsystemspring.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.parent.entity.ParentType;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.parent.entity.Parent}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResponseParent(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String description,
        boolean isEnabled,
        ParentType type,
        String occupation,
        StudentDto studentResponsible
) implements Serializable {

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.student.entity.Student}
     */

    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record StudentDto(
            Integer id,
            String firstName,
            String lastName,
            String email
    ) implements Serializable { }
}