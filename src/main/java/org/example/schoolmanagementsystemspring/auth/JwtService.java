package org.example.schoolmanagementsystemspring.auth;

import org.example.schoolmanagementsystemspring.user.User;

import java.util.Date;
import java.util.Map;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public interface JwtService {
    String generateToken(User user, Map<String, Object> claims);

    String generateRefreshToken(User user);

    boolean isTokenValid(String token, User user);

    Date getExpirationDate(String token);

    String getSubject(String token);

}
