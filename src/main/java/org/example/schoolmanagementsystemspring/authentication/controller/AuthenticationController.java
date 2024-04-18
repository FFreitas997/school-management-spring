package org.example.schoolmanagementsystemspring.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponse;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Authentication", description = "Endpoints for authentication and registration in the system.")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(
            summary = "Register a new user",
            description = "Register a new user in the system.",
            method = "POST",
            tags = {"Authentication and Register System"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User information to register.",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RequestRegisterDTO.class)
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AuthenticationResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request. User already exists."
                    )
            }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> register(@Valid @RequestBody RequestRegisterDTO requestBody) {
        log.info("Registering user: {}", requestBody.email());
        service.register(requestBody);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Authenticate a user",
            description = "Authenticate a user in the system.",
            method = "POST",
            tags = {"Authentication and Register System"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User information to authenticate.",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationRequestDto.class)
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AuthenticationResponse.class)
                                    )
                            }
                    )
            }
    )
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequestDto requestBody) {
        log.info("Authenticating user: {}", requestBody.email());
        return service.authenticate(requestBody);
    }

    @Operation(
            summary = "Refresh token",
            description = "Refresh the token of a user in the system.",
            method = "POST",
            tags = {"Authentication and Register System"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token refreshed successfully.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AuthenticationResponse.class)
                                    )
                            }
                    )
            }
    )
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Refreshing token");
        service.refreshToken(request, response);
    }

    @GetMapping("/confirm-account")
    public void confirmAccount() {
        log.info("Confirming account");
    }
}
