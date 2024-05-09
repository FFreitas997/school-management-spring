package org.example.schoolmanagementsystemspring.teacher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherAlreadyExistsException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherNotFoundException;
import org.example.schoolmanagementsystemspring.teacher.dto.RequestTeacher;
import org.example.schoolmanagementsystemspring.teacher.dto.TeacherResponse;
import org.example.schoolmanagementsystemspring.teacher.service.TeacherService;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teacher")
@Tag(name = "Teacher Features", description = "These operations allow the Teacher user to manage their account.")
public class TeacherController {

    private final TeacherService service;

    @PostMapping
    @Operation(summary = "Teacher Registration", description = "Allow the Teacher user to register in the system.")
    @ResponseStatus(ACCEPTED)
    public void registerTeacher(@RequestBody @Valid RequestTeacher request) throws UserNotFoundException, UserAlreadyExistsException, SchoolNotFoundException, TeacherAlreadyExistsException {
        log.info("Registering teacher: {}", request.email());
        service.register(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Teacher Information", description = "Get Teacher Information")
    @ResponseStatus(OK)
    public TeacherResponse getTeacher(Authentication authentication) throws TeacherNotFoundException {
        log.info("Getting teacher: {}", authentication.getName());
        return service.getTeacherInformation(authentication);
    }
}
