package org.example.schoolmanagementsystemspring.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.event.EventType;

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
        String location
) implements Serializable {
}