package org.example.schoolmanagementsystemspring.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Users Management System", description = "Endpoints for managing users in the system.")
@SecurityRequirement(name = "JSON Web Token (JWT)")
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Get all users",
            description = "Get all users in the system.",
            method = "GET",
            tags = {"Users Management System"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of all users in the system.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = User.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You don't have admin permission to access this resource."
                    )
            }
    )
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    // TODO upload picture endpoint and download picture endpoint
}
