package org.example.schoolmanagementsystemspring.authentication.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponse;
import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.exception.InvalidTokenException;
import org.example.schoolmanagementsystemspring.authentication.exception.TokenNotFoundException;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;

import java.io.IOException;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface AuthenticationService {

    void register(RequestRegisterDTO requestBody) throws UserAlreadyExistsException;

    AuthenticationResponse authenticate(AuthenticationRequestDto requestBody) throws UserNotFoundException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void confirmAccount(String token) throws TokenNotFoundException, InvalidTokenException, UserNotFoundException;

    void generateActivationCode(String email) throws UserNotFoundException;
}
