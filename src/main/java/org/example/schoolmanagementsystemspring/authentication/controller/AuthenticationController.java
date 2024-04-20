package org.example.schoolmanagementsystemspring.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.example.schoolmanagementsystemspring.authentication.exception.InvalidTokenException;
import org.example.schoolmanagementsystemspring.authentication.exception.TokenNotFoundException;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.exception.ExceptionResponse;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
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
                            responseCode = "409",
                            description = "User already exists.",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Generic error",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RequestRegisterDTO requestBody) throws UserAlreadyExistsException {
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Generic error",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequestDto requestBody) throws UserNotFoundException {
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
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Generic error",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Refreshing token");
        service.refreshToken(request, response);
    }

    @Operation(
            summary = "Activate account",
            description = "Activate the account of a user in the system.",
            method = "PUT",
            tags = {"Authentication and Register System"},
            parameters = {
                    @Parameter(
                            name = "code",
                            description = "Activation code to activate the account.",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account activated successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found.",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid Token",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Token Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Generic error",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    )
            }
    )
    @PutMapping("/activate-account")
    public ResponseEntity<Object> confirmAccount(@RequestParam String code) throws UserNotFoundException, InvalidTokenException, TokenNotFoundException {
        service.confirmAccount(code);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Generate activation code",
            description = "Generate an activation code for a user in the system.",
            method = "POST",
            tags = {"Authentication and Register System"},
            parameters = {
                    @Parameter(
                            name = "email",
                            description = "Email to generate the activation code.",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Activation code generated successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found.",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Generic error.",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/generate-activation-code")
    public ResponseEntity<Object> generateActivationCode(@RequestParam String email) throws UserNotFoundException {
        service.generateActivationCode(email);
        return ResponseEntity.accepted().build();
    }
}
