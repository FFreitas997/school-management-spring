package org.example.schoolmanagementsystemspring.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.admin.service.AdminService;
import org.example.schoolmanagementsystemspring.admin.dto.AdminRequestRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Register Admin", description = "Admin User Registration")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(@Valid @RequestBody AdminRequestRegisterDTO request) throws UserAlreadyExistsException, UserNotFoundException {
        log.info("Registering Admin user: {}", request.email());
        service.register(request);
    }
}