package org.example.schoolmanagementsystemspring.mail;

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
public enum EmailTemplate {

    WELCOME("welcome"),
    RESET_PASSWORD("reset-password"),
    CONFIRM_EMAIL("confirm-email");

    private final String templateName;
}
