package org.example.schoolmanagementsystemspring.authentication.service;

import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.user.entity.User;

import java.util.Date;
import java.util.Map;

/**
 * The JwtService interface provides the contract for the JWT service.
 * It includes methods for generating tokens, validating tokens, getting the expiration date of a token, and getting the subject of a token.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface JwtService {

    /**
     * The generateToken method generates a token for a user with the provided claims.
     *
     * @param user the user for whom the token is to be generated.
     * @param claims a map containing the claims to be included in the token.
     * @return a string representing the generated token.
     */
    String generateToken(User user, Map<String, Object> claims);

    /**
     * The generateRefreshToken method generates a refresh token for a user.
     *
     * @param user the user for whom the refresh token is to be generated.
     * @return a string representing the generated refresh token.
     */
    String generateRefreshToken(User user);

    /**
     * The isTokenValid method checks if a token is valid for a user.
     *
     * @param token the token to be checked.
     * @param user the user for whom the token is to be checked.
     * @return a boolean indicating whether the token is valid.
     */
    boolean isTokenValid(Token token, User user);

    /**
     * The getExpirationDate method gets the expiration date of a token.
     *
     * @param token the token whose expiration date is to be retrieved.
     * @return a Date object representing the expiration date of the token.
     */
    Date getExpirationDate(String token);

    /**
     * The getSubject method gets the subject of a token.
     *
     * @param token the token whose subject is to be retrieved.
     * @return a string representing the subject of the token.
     */
    String getSubject(String token);
}