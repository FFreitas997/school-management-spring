package org.example.schoolmanagementsystemspring.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Getter
@RequiredArgsConstructor
public enum EmailTemplate {

    WELCOME("welcome"),
    RESET_PASSWORD("reset-password"),
    CONFIRM_EMAIL("confirm-email");

    private final String templateName;
}
