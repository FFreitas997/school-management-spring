package org.example.schoolmanagementsystemspring.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Authentication and Register System", description = "Endpoints for authentication and registration in the system.")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
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
                                            schema = @Schema(implementation = ResponseRegisterDTO.class)
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseRegisterDTO register(@RequestBody RequestRegisterDTO requestBody) throws Exception {
        log.info("Registering user: {}", requestBody.email());
        return service.register(requestBody);
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
                                            schema = @Schema(implementation = AuthenticationResponseDto.class)
                                    )
                            }
                    )
            }
    )
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto requestBody) {
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
                                            schema = @Schema(implementation = AuthenticationResponseDto.class)
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
}
