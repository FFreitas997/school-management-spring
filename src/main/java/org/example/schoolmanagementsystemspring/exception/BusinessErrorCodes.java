package org.example.schoolmanagementsystemspring.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Getter
@RequiredArgsConstructor
public enum BusinessErrorCodes {
    NO_CODE(NOT_IMPLEMENTED, "No code defined"),
    INCORRECT_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "New password does not match"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already exists"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
    ACCOUNT_LOCKED(HttpStatus.LOCKED, "Account locked"),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "Account disabled"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token expired"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Token not found");

    private final HttpStatus httpStatus;
    private final String description;
}
