package org.example.schoolmanagementsystemspring.exception;

import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@ControllerAdvice
public class GlobalHandlerExceptions {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorValidation> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HashMap<String, String> map = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    map.put(fieldName, errorMessage);
                });
        ErrorValidation error = ErrorValidation.builder()
                .message("Validation Error")
                .status(HttpStatus.BAD_REQUEST.value())
                .current(LocalDateTime.now())
                .fields(map)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorCustomResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorCustomResponse error = ErrorCustomResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .path("/api/v1/users")
                .error(UserNotFoundException.class.getSimpleName())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
