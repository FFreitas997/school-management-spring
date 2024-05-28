package org.example.schoolmanagementsystemspring.teacher.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.textbook.TextBook}
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TextBookResponse(
        Integer id,
        String title,
        String description,
        String author,
        String edition,
        String isbn,
        Integer cost,
        String link
) implements Serializable { }