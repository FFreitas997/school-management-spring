package org.example.schoolmanagementsystemspring.school.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.event.EventType;
import org.example.schoolmanagementsystemspring.school.entity.SchoolType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.event.Event}
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record EventResponse(
        Integer id,
        String title,
        String description,
        LocalDateTime start,
        LocalDateTime end,
        EventType type,
        String location,
        SchoolDto school
) implements Serializable {

    /**
     * DTO for {@link org.example.schoolmanagementsystemspring.school.entity.School}
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record SchoolDto(
            String email,
            String facebookURL,
            String instagramURL,
            String address,
            String city,
            String zipCode,
            Integer id,
            String name,
            SchoolType schoolType
    ) implements Serializable { }
}