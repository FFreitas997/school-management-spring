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
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
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

    @Override
    public boolean isTokenValid(Token token, User user) {
        if (token == null || user == null)
            return false;
        return isSubjectValid(token.getValue(), user.getUsername()) && isTokenNonExpired(token.getValue());
    }

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

    private SecretKey getSignInKey() {
        byte[] secretBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    private boolean isSubjectValid(String token, String username) {
        return getSubject(token).equals(username);
    }

    private boolean isTokenNonExpired(String token) {
        return getExpirationDate(token).after(new Date());
    }
}
