package org.example.schoolmanagementsystemspring.student.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.student.dto.*;
import org.example.schoolmanagementsystemspring.student.service.RequestStudent;
import org.example.schoolmanagementsystemspring.student.service.StudentService;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
@Tag(name = "Student Features", description = "These operations are related to students.")
public class StudentController {

    private final StudentService service;

    @Operation(summary = "Register a student", description = "This operation registers a student.")
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void register(@RequestBody @Valid RequestStudent request) throws UserNotFoundException, SchoolNotFoundException {
        log.info("Registering a student: {}", request.email());
        service.register(request);
    }

    @PreAuthorize("hasRole('STUDENT') and hasAuthority('student:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get student information", description = "This operation retrieves the student information.")
    @GetMapping
    public StudentInformation getStudentInformation(Authentication authentication) throws UserNotFoundException {
        log.info("Retrieving information about the student: {}", authentication.getName());
        return service.getStudentInformation(authentication);
    }

    @PreAuthorize("hasRole('STUDENT') and hasAuthority('student:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get enrolled courses", description = "This operation retrieves the courses in which the student is enrolled.")
    @GetMapping("/courses")
    public Page<CourseResponse> enrolledCourses(Authentication authentication, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("Retrieving the courses in which the student is enrolled: {}", authentication.getName());
        return service.enrolledCourses(authentication, page, size);
    }

    @PreAuthorize("hasRole('STUDENT') and hasAuthority('student:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get all assignments", description = "This operation retrieves all student assignments.")
    @GetMapping("/assignments")
    public Page<AssignmentResponse> getAllAssignments(Authentication authentication, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UserNotFoundException {
        log.info("Retrieving all student assignments: {}", authentication.getName());
        return service.getAllAssignments(authentication, page, size);
    }

    @PreAuthorize("hasRole('STUDENT') and hasAuthority('student:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get future assignments", description = "This operation retrieves the student's future assignments.")
    @GetMapping("/assignments/future")
    public List<AssignmentResponse> getFutureAssignments(Authentication authentication) throws UserNotFoundException {
        log.info("Retrieving the student's future assignments: {}", authentication.getName());
        return service.getFutureAssignments(authentication);
    }

    @PreAuthorize("hasRole('STUDENT') and hasAuthority('student:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get teacher responsible for", description = "This operation retrieves the teacher responsible for the student.")
    @GetMapping("/teacher")
    public TeacherResponse getTeacherResponsibleFor(Authentication authentication) throws UserNotFoundException {
        log.info("Retrieving the teacher responsible for the student: {}", authentication.getName());
        return service.getTeacherResponsibleFor(authentication);
    }

    @PreAuthorize("hasRole('STUDENT') and hasAuthority('student:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get parent information", description = "This operation retrieves the student's parent information.")
    @GetMapping("/parent")
    public ParentResponse getParentInformation(Authentication authentication) throws UserNotFoundException {
        log.info("Retrieving the student's parent information: {}", authentication.getName());
        return service.getParentInformation(authentication);
    }
}
