package org.example.schoolmanagementsystemspring.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Users Management System", description = "Endpoints for managing users in the system.")
@SecurityRequirement(name = "JSON Web Token (JWT)")
@Slf4j
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Get all users",
            description = "Get all users in the system.",
            method = "GET",
            tags = {"Users Management System"}

    )
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "firstName") String sort,
                                     @RequestParam(defaultValue = "asc") String order
    ) {
        log.info("Getting all users in the system.");
        return service.getAllUsers(page, size, sort, order);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Get user by ID in the system.",
            method = "GET",
            tags = {"Users Management System"}
    )
    @GetMapping("/{userID}")
    @PreAuthorize("hasAuthority('admin:read')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Integer userID) throws UserNotFoundException {
        log.info("Getting user by ID: {}", userID);
        return service.getUserById(userID);
    }

    @Operation(
            summary = "Create user",
            description = "Create user in the system.",
            method = "POST",
            tags = {"Users Management System"}
    )
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRequestDto request) {
        log.info("Creating user: {}", request);
        return service.createUser(request);
    }

    @Operation(
            summary = "Update user",
            description = "Update user in the system.",
            method = "PUT",
            tags = {"Users Management System"}

    )
    @PutMapping("/{userID}")
    @PreAuthorize("hasAuthority('admin:update')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable Integer userID, @Valid @RequestBody UserRequestDto request) throws UserNotFoundException {
        log.info("Updating user with ID {}", userID);
        return service.updateUser(userID, request);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete user in the system.",
            method = "DELETE",
            tags = {"Users Management System"}
    )
    @DeleteMapping("/{userID}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userID) {
        log.info("Deleting user with ID {}", userID);
        service.deleteUser(userID);
    }
}
