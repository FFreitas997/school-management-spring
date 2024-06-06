package org.example.schoolmanagementsystemspring.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.admin.dto.*;
import org.example.schoolmanagementsystemspring.admin.service.AdminService;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;

/**
 * The AdminController class is a REST controller that handles HTTP requests related to admin operations.
 * It is secured with JWT and only accessible to users with the 'ADMIN' role.
 * It uses the AdminService to perform the business logic related to admin operations.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "JSON Web Token (JWT)")
@Tag(name = "Admin Operations", description = "These operations are specific to admin users")
public class AdminController {

    /**
     * The AdminService is injected into this controller using constructor injection.
     * It provides the business logic for admin operations.
     */
    private final AdminService service;

    /**
     * The register method handles POST requests to /api/v1/admin/register.
     * It registers a new admin user with the details provided in the request body.
     * The request body must be a valid AdminRequestRegisterDTO object.
     * If the user already exists, it throws a UserAlreadyExistsException.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param request a valid AdminRequestRegisterDTO object containing the details of the new admin user.
     * @throws UserAlreadyExistsException if the user already exists.
     * @throws UserNotFoundException      if the user is not found.
     */
    @Operation(summary = "Register User", description = "Register a new admin user with the provided details.")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/register")
    @ResponseStatus(ACCEPTED)
    public void register(@Valid @RequestBody RequestUser request) throws UserAlreadyExistsException, UserNotFoundException {
        log.info("Registering user: {}", request.email());
        service.register(request);
    }

    @Operation(summary = "Get Teachers", description = "Get a list of teachers.")
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/teachers")
    public Page<ResponseTeacher> getTeachers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Getting teachers for page: {} and size: {}", page, size);
        return service.getTeachers(page, size);
    }

    @Operation(summary = "Get Students", description = "Get a list of students.")
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/students")
    public Page<ResponseStudent> getStudents(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Getting students for page: {} and size: {}", page, size);
        return service.getStudents(page, size);
    }

    @Operation(summary = "Get Parents", description = "Get a list of parents.")
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/parents")
    public Page<ResponseParent> getParents(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Getting parents for page: {} and size: {}", page, size);
        return service.getParents(page, size);
    }

    @Operation(summary = "Get Courses", description = "Get a list of courses.")
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/courses")
    public Page<ResponseCourse> getCourses(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Getting courses for page: {} and size: {}", page, size);
        return service.getCourses(page, size);
    }

    @Operation(summary = "Create School", description = "Create a new school.")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/school")
    @ResponseStatus(ACCEPTED)
    public void createSchool(@Valid @RequestBody RequestSchool request) {
        log.info("Creating school: {}", request.name());
        service.createSchool(request);
    }

    @Operation(summary = "Get Schools", description = "Get a list of schools.")
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/schools")
    public List<SchoolResponse> getSchools() {
        log.info("Getting schools");
        return service.getSchools();
    }

    @Operation(summary = "Create Event", description = "Create a new event.")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/event")
    @ResponseStatus(ACCEPTED)
    public void createEvent(@Valid @RequestBody RequestEvent request) {
        log.info("Creating event: {}", request.title());
        service.createEvent(request);
    }

    @Operation(summary = "Get Events", description = "Get a list of events.")
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/events")
    public Page<EventResponse> getEvents(@RequestParam Integer schoolId, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Getting events for schoolId: {}, page: {} and size: {}", schoolId, page, size);
        return service.getEvents(schoolId, page, size);
    }

    @Operation(summary = "Create Course", description = "Create a new course.")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/course")
    @ResponseStatus(ACCEPTED)
    public void createCourse(@Valid @RequestBody RequestCourse request) {
        log.info("Creating course: {}", request.courseName());
        service.createCourse(request);
    }

    @Operation(summary = "Associate Teacher to Course", description = "Associate a teacher to a course.")
    @PreAuthorize("hasAuthority('admin:update')")
    @PatchMapping("/course/associate")
    @ResponseStatus(ACCEPTED)
    public void associateTeacherToCourse(@RequestParam String courseCode, @RequestParam String teacherEmail) {
        log.info("Associating teacher: {} to course: {}", teacherEmail, courseCode);
        service.associateTeacherToCourse(courseCode, teacherEmail);
    }
}