package org.example.schoolmanagementsystemspring.authentication.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Getter
@RequiredArgsConstructor
public enum TokenType {
    BEARER("Bearer"),
    ACTIVATION_CODE("Activation Code");

    private final String value;
}
