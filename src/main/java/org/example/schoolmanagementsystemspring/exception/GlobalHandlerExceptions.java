package org.example.schoolmanagementsystemspring.exception;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.exception.InvalidTokenException;
import org.example.schoolmanagementsystemspring.authentication.exception.TokenNotFoundException;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.example.schoolmanagementsystemspring.exception.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@RestControllerAdvice
public class GlobalHandlerExceptions {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException ex) {
        return ResponseEntity
                .status(ACCOUNT_LOCKED.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(ACCOUNT_LOCKED.getHttpStatus().value())
                                .description(ACCOUNT_LOCKED.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException ex) {
        return ResponseEntity
                .status(ACCOUNT_DISABLED.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(ACCOUNT_DISABLED.getHttpStatus().value())
                                .description(ACCOUNT_DISABLED.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException ex) {
        return ResponseEntity
                .status(INVALID_CREDENTIALS.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(INVALID_CREDENTIALS.getHttpStatus().value())
                                .description(INVALID_CREDENTIALS.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException ex) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse
                                .builder()
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(USER_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(USER_NOT_FOUND.getHttpStatus().value())
                                .description(USER_NOT_FOUND.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException ex) {
        return ResponseEntity
                .status(USER_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(USER_NOT_FOUND.getHttpStatus().value())
                                .description(USER_NOT_FOUND.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(USER_ALREADY_EXISTS.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(USER_ALREADY_EXISTS.getHttpStatus().value())
                                .description(USER_ALREADY_EXISTS.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(TokenNotFoundException ex) {
        return ResponseEntity
                .status(TOKEN_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(TOKEN_NOT_FOUND.getHttpStatus().value())
                                .description(TOKEN_NOT_FOUND.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidTokenException ex) {
        return ResponseEntity
                .status(INVALID_TOKEN.getHttpStatus())
                .body(
                        ExceptionResponse
                                .builder()
                                .errorCode(INVALID_TOKEN.getHttpStatus().value())
                                .description(INVALID_TOKEN.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException ex) {
        Set<String> errors = new HashSet<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> errors.add(((FieldError) error).getField() + ": " + error.getDefaultMessage()));
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse
                                .builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        log.error("An {} error occurred: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse
                                .builder()
                                .description("An error occurred, please contact the system administrator")
                                .error(ex.getMessage())
                                .build()
                );
    }
}
