package org.example.schoolmanagementsystemspring.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponse;
import org.example.schoolmanagementsystemspring.authentication.exception.InvalidTokenException;
import org.example.schoolmanagementsystemspring.authentication.exception.TokenNotFoundException;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * The AuthenticationController class is a REST controller that handles HTTP requests related to user authentication.
 * It uses the AuthenticationService to perform the business logic related to authentication operations.
 * It handles endpoints for user authentication, token refresh, account activation, and activation code generation.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "These operations are related to the Authentication")
public class AuthenticationController {

    /**
     * The AuthenticationService is injected into this controller using constructor injection.
     * It provides the business logic for authentication operations.
     */
    private final AuthenticationService service;

    /**
     * The authenticate method handles POST requests to /api/v1/auth/authenticate.
     * It authenticates a user with the details provided in the request body.
     * The request body must be a valid AuthenticationRequestDto object.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param request a valid AuthenticationRequestDto object containing the details of the user to be authenticated.
     * @return an AuthenticationResponse object containing the authentication details.
     * @throws UserNotFoundException if the user is not found.
     */
    @Operation(summary = "Authenticate a user", description = "Authenticate a user in the system")
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequestDto request) throws UserNotFoundException {
        log.info("Authenticating user: {}", request.email());
        return service.authenticate(request);
    }

    /**
     * The refreshToken method handles POST requests to /api/v1/auth/refresh-token.
     * It refreshes the token of the user making the request.
     * It does not require any request body.
     *
     * @param request  the HttpServletRequest object containing the details of the request.
     * @param response the HttpServletResponse object for sending the response.
     * @throws IOException if an input or output exception occurred.
     */
    @Operation(summary = "Refresh token", description = "Refresh the token of a user in the system.")
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Refreshing token ...");
        service.refreshToken(request, response);
    }

    /**
     * The confirmAccount method handles PUT requests to /api/v1/auth/activate-account.
     * It activates the account of the user with the provided activation code.
     * If the user is not found, it throws a UserNotFoundException.
     * If the token is invalid, it throws an InvalidTokenException.
     * If the token is not found, it throws a TokenNotFoundException.
     *
     * @param code the activation code of the user.
     * @throws UserNotFoundException  if the user is not found.
     * @throws InvalidTokenException  if the token is invalid.
     * @throws TokenNotFoundException if the token is not found.
     */
    @Operation(summary = "Activate account", description = "Activate the account of a user in the system.")
    @PutMapping("/activate-account")
    @ResponseStatus(HttpStatus.OK)
    public void confirmAccount(@RequestParam String code) throws UserNotFoundException, InvalidTokenException, TokenNotFoundException {
        log.info("Activating account ...");
        service.confirmAccount(code);
    }

    /**
     * The generateActivationCode method handles POST requests to /api/v1/auth/generate-activation-code.
     * It generates an activation code for the user with the provided email.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param email the email of the user.
     * @throws UserNotFoundException if the user is not found.
     */
    @Operation(summary = "Generate activation code", description = "Generate an activation code for a user in the system.")
    @PostMapping("/generate-activation-code")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generateActivationCode(@RequestParam String email) throws UserNotFoundException {
        log.info("Generating activation code ...");
        service.generateActivationCode(email);
    }
}