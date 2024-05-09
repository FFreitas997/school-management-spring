package org.example.schoolmanagementsystemspring.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * The UserController class is a REST controller that handles HTTP requests related to users.
 * It uses the UserService to perform its operations.
 * It provides endpoints for getting all users, getting a user by ID, creating a user, updating a user, deleting a user, uploading a user image, and downloading a user image.
 * It uses the @RestController, @RequestMapping, @RequiredArgsConstructor, @Tag, and @SecurityRequirement annotations to define its behavior.
 * It uses the @Operation annotation to document its endpoints.
 * It uses the @PreAuthorize annotation to secure its endpoints.
 * It uses the @ResponseStatus annotation to define the HTTP status code returned by its endpoints.
 * It uses the @GetMapping, @PostMapping, @PutMapping, and @DeleteMapping annotations to map HTTP requests to its methods.
 * It uses the @RequestParam, @PathVariable, @RequestBody, and @RequestPart annotations to bind method parameters to HTTP request parameters, path variables, request bodies, and request parts, respectively.
 * It uses the @Valid annotation to validate method parameters.
 * It uses the log object to log information about its operations.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users Management System", description = "Endpoints for managing users in the system.")
@SecurityRequirement(name = "JSON Web Token (JWT)")
@Slf4j
public class UserController {

    private final UserService service;

    /**
     * The getAllUsers method handles GET requests to get all users.
     * It uses the UserService to get all users.
     * It returns a Page of UserDto objects.
     * It logs the operation.
     *
     * @param page the page number.
     * @param size the page size.
     * @return a Page of UserDto objects.
     */
    @Operation(summary = "Get all users", description = "Get all users in the system.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:read')")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all users in the system.");
        return service.getAllUsers(page, size);
    }

    /**
     * The getUserById method handles GET requests to get a user by ID.
     * It uses the UserService to get the user.
     * It returns a UserDto object.
     * It logs the operation.
     *
     * @param userID the ID of the user.
     * @return a UserDto object.
     * @throws UserNotFoundException if the user is not found.
     */
    @Operation(summary = "Get user by ID", description = "Get user by ID in the system.")
    @GetMapping("/{userID}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:read')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Integer userID) throws UserNotFoundException {
        log.info("Getting user by ID: {}", userID);
        return service.getUserById(userID);
    }

    /**
     * The createUser method handles POST requests to create a user.
     * It uses the UserService to create the user.
     * It returns a UserDto object.
     * It logs the operation.
     *
     * @param request the UserRequestDto object that contains the user data.
     * @return a UserDto object.
     */
    @Operation(summary = "Create user", description = "Create user in the system.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:create')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRequestDto request) {
        log.info("Creating user: {}", request);
        return service.createUser(request);
    }

    /**
     * The updateUser method handles PUT requests to update a user.
     * It uses the UserService to update the user.
     * It returns a UserDto object.
     * It logs the operation.
     *
     * @param userID  the ID of the user.
     * @param request the UserRequestDto object that contains the new user data.
     * @return a UserDto object.
     * @throws UserNotFoundException if the user is not found.
     */
    @Operation(summary = "Update user", description = "Update user in the system.")
    @PutMapping("/{userID}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:update')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable Integer userID, @Valid @RequestBody UserRequestDto request) throws UserNotFoundException {
        log.info("Updating user with ID {}", userID);
        return service.updateUser(userID, request);
    }

    /**
     * The deleteUser method handles DELETE requests to delete a user.
     * It uses the UserService to delete the user.
     * It logs the operation.
     *
     * @param userID the ID of the user.
     */
    @Operation(summary = "Delete user", description = "Delete user in the system.")
    @DeleteMapping("/{userID}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userID) {
        log.info("Deleting user with ID {}", userID);
        service.deleteUser(userID);
    }

    /**
     * The uploadUserImage method handles POST requests to upload a user image.
     * It uses the UserService to upload the user image.
     * It logs the operation.
     *
     * @param file the MultipartFile object that contains the user image.
     * @param auth the Authentication object that contains the user authentication.
     * @throws IOException           if an I/O error occurs.
     * @throws UserNotFoundException if the user is not found.
     */
    @Operation(summary = "Upload User Image", description = "Upload user image in the system.")
    @PostMapping(value = "/profile-image", consumes = MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @ResponseStatus(ACCEPTED)
    public void uploadUserImage(@Parameter() @RequestPart("file") MultipartFile file, Authentication auth) throws IOException, UserNotFoundException {
        log.info("Uploading user {} image", auth.getName());
        service.uploadUserImage(auth, file);
    }

    /**
     * The downloadUserImage method handles GET requests to download a user image.
     * It uses the UserService to download the user image.
     * It returns a ResponseEntity that contains the user image.
     * It logs the operation.
     *
     * @param auth the Authentication object that contains the user authentication.
     * @return a ResponseEntity that contains the user image.
     * @throws UserNotFoundException if the user is not found.
     */
    @Operation(summary = "Download User Image", description = "Download user image in the system.")
    @GetMapping(value = "/profile-image")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<Resource> downloadUserImage(Authentication auth) throws UserNotFoundException {
        log.info("Download user {} image", auth.getName());
        Resource result = service.downloadUserImage(auth);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + result.getFilename() + "\"")
                .contentType(MediaType.parseMediaType("image/png"))
                .body(result);
    }
}