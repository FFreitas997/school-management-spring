package org.example.schoolmanagementsystemspring.authentication.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponseDto;
import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.dto.ResponseRegisterDTO;

import java.io.IOException;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public interface AuthenticationService {

    ResponseRegisterDTO register(RequestRegisterDTO requestBody) throws Exception;

    AuthenticationResponseDto authenticate(AuthenticationRequestDto requestBody);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
