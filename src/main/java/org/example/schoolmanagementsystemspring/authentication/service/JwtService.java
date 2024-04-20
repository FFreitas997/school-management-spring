package org.example.schoolmanagementsystemspring.authentication.service;

import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.user.entity.User;

import java.util.Date;
import java.util.Map;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface JwtService {
    String generateToken(User user, Map<String, Object> claims);

    String generateRefreshToken(User user);

    boolean isTokenValid(Token token, User user);

    Date getExpirationDate(String token);

    String getSubject(String token);

}
