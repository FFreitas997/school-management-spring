package org.example.schoolmanagementsystemspring.school.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.schoolmanagementsystemspring.event.EventType;

import java.time.LocalDateTime;

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
) { }
