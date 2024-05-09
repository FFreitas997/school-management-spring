package org.example.schoolmanagementsystemspring.authentication.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponse;
import org.example.schoolmanagementsystemspring.authentication.exception.InvalidTokenException;
import org.example.schoolmanagementsystemspring.authentication.exception.TokenNotFoundException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;

import java.io.IOException;

/**
 * The AuthenticationService interface provides the contract for the authentication service.
 * It includes methods for user authentication, token refresh, account activation, and activation code generation.
 * Each method throws specific exceptions that are handled by the controller.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface AuthenticationService {

    /**
     * The authenticate method authenticates a user with the details provided in the request body.
     * The request body must be a valid AuthenticationRequestDto object.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param requestBody a valid AuthenticationRequestDto object containing the details of the user to be authenticated.
     * @return an AuthenticationResponse object containing the authentication details.
     * @throws UserNotFoundException if the user is not found.
     */
    AuthenticationResponse authenticate(AuthenticationRequestDto requestBody) throws UserNotFoundException;

    /**
     * The refreshToken method refreshes the token of the user making the request.
     * It does not require any request body.
     *
     * @param request  the HttpServletRequest object containing the details of the request.
     * @param response the HttpServletResponse object for sending the response.
     * @throws IOException if an input or output exception occurred.
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * The confirmAccount method activates the account of the user with the provided activation code.
     * If the user is not found, it throws a UserNotFoundException.
     * If the token is invalid, it throws an InvalidTokenException.
     * If the token is not found, it throws a TokenNotFoundException.
     *
     * @param token the activation code of the user.
     * @throws UserNotFoundException  if the user is not found.
     * @throws InvalidTokenException  if the token is invalid.
     * @throws TokenNotFoundException if the token is not found.
     */
    void confirmAccount(String token) throws TokenNotFoundException, InvalidTokenException, UserNotFoundException;

    /**
     * The generateActivationCode method generates an activation code for the user with the provided email.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param email the email of the user.
     * @throws UserNotFoundException if the user is not found.
     */
    void generateActivationCode(String email) throws UserNotFoundException;
}