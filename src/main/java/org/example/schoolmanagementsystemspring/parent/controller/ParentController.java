package org.example.schoolmanagementsystemspring.parent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.parent.dto.ParentResponse;
import org.example.schoolmanagementsystemspring.parent.dto.RequestParent;
import org.example.schoolmanagementsystemspring.parent.dto.StudentResponse;
import org.example.schoolmanagementsystemspring.parent.service.ParentService;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parent")
@Tag(name = "Parent Features", description = "These operations are related to the parent of the student.")
public class ParentController {

    private final ParentService service;

    @Operation(summary = "Register a parent", description = "This operation is used to register a parent.")
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void registerParent(@RequestBody @Valid RequestParent request) throws UserNotFoundException, UserAlreadyExistsException, SchoolNotFoundException {
        log.info("Registering a parent: {}", request.email());
        service.register(request);
    }

    @PreAuthorize("hasRole('PARENT') and hasAuthority('parent:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Parent Information", description = "This operation is used to get the parent information.")
    @GetMapping
    @ResponseStatus(OK)
    public ParentResponse getParentInformation(Authentication authentication) throws UserNotFoundException {
        log.info("Getting parent information for: {}", authentication.getName());
        return service.getParentInformation(authentication);
    }

    @PreAuthorize("hasRole('PARENT') and hasAuthority('parent:update')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Associate a student", description = "This operation is used to associate a student to a parent.")
    @PatchMapping("/student")
    @ResponseStatus(ACCEPTED)
    public void associateStudentForParent(Authentication authentication, @RequestParam String studentEmail) throws UserNotFoundException {
        log.info("Associating student for parent: {}", authentication.getName());
        service.associateStudentFor(authentication, studentEmail);
    }

    @PreAuthorize("hasRole('PARENT') and hasAuthority('parent:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get student information", description = "This operation is used to get the student information.")
    @GetMapping("/student")
    @ResponseStatus(OK)
    public StudentResponse getStudentForParent(Authentication authentication) throws UserNotFoundException {
        log.info("Getting student information for parent: {}", authentication.getName());
        return service.getStudentFor(authentication);
    }
}
