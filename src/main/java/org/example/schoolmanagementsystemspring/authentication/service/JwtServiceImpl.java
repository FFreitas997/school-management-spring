package org.example.schoolmanagementsystemspring.authentication.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * The JwtServiceImpl class is a service that implements the JwtService interface.
 * It provides methods for generating tokens, validating tokens, getting the expiration date of a token, and getting the subject of a token.
 * It uses the Jwts library to perform its operations.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${spring.application.security.jwt.secret}")
    private String secretKey;

    @Value("${spring.application.security.jwt.expiration}")
    private long expireIn;

    @Value("${spring.application.security.jwt.refresh-token-expiration}")
    private long expireRefreshTokenIn;

    @Value("${spring.application.name}")
    private String issuer;

    /**
     * The generateToken method generates a token for a user with the provided claims.
     *
     * @param user   the user for whom the token is to be generated.
     * @param claims a map containing the claims to be included in the token.
     * @return a string representing the generated token.
     */
    @Override
    public String generateToken(User user, Map<String, Object> claims) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + expireIn))
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .claim("authorities", user.getRole().getAuthorities())
                .audience().add(user.getRole().name()).and()
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * The generateRefreshToken method generates a refresh token for a user.
     *
     * @param user the user for whom the refresh token is to be generated.
     * @return a string representing the generated refresh token.
     */
    @Override
    public String generateRefreshToken(User user) {
        return Jwts
                .builder()
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + expireRefreshTokenIn))
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .claim("authorities", user.getRole().getAuthorities())
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * The isTokenValid method checks if a token is valid for a user.
     *
     * @param token the token to be checked.
     * @param user  the user for whom the token is to be checked.
     * @return a boolean indicating whether the token is valid.
     */
    @Override
    public boolean isTokenValid(Token token, User user) {
        if (token == null || user == null)
            return false;
        return isSubjectValid(token.getValue(), user.getUsername()) && isTokenNonExpired(token.getValue());
    }

    /**
     * The getExpirationDate method gets the expiration date of a token.
     *
     * @param token the token whose expiration date is to be retrieved.
     * @return a Date object representing the expiration date of the token.
     */
    @Override
    public Date getExpirationDate(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * The getSubject method gets the subject of a token.
     *
     * @param token the token whose subject is to be retrieved.
     * @return a string representing the subject of the token.
     */
    @Override
    public String getSubject(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * The getSignInKey method gets the signing key for the tokens.
     *
     * @return a SecretKey object representing the signing key.
     */
    private SecretKey getSignInKey() {
        byte[] secretBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * This method checks if the subject of a token matches the provided username.
     * It retrieves the subject from the token and compares it with the provided username.
     * If they match, it returns true; otherwise, it returns false.
     *
     * @param token    the token whose subject is to be checked.
     * @param username the username to be compared with the subject of the token.
     * @return a boolean indicating whether the subject of the token matches the provided username.
     */
    private boolean isSubjectValid(String token, String username) {
        return getSubject(token).equals(username);
    }

    /**
     * This method checks if a token is not expired.
     * It retrieves the expiration date of the token and compares it with the current date.
     * If the expiration date is after the current date, it returns true; otherwise, it returns false.
     *
     * @param token the token whose expiration date is to be checked.
     * @return a boolean indicating whether the token is not expired.
     */
    private boolean isTokenNonExpired(String token) {
        return getExpirationDate(token).after(new Date());
    }
}
