package org.example.schoolmanagementsystemspring.authentication.mapper;

import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Component
public class TokenAuthMapper implements BiFunction<String, User, Token> {
    @Override
    public Token apply(String token, User user) {
        return Token
                .builder()
                .value(token)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
    }
}
