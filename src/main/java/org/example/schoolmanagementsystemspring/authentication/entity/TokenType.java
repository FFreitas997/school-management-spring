package org.example.schoolmanagementsystemspring.authentication.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Getter
@RequiredArgsConstructor
public enum TokenType {
    BEARER("Bearer"),
    ACTIVATION_CODE("Activation Code");

    private final String value;
}
