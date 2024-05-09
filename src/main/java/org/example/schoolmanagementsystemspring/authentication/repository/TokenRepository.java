package org.example.schoolmanagementsystemspring.authentication.repository;

import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The TokenRepository interface is a Spring Data JPA repository for the Token entity.
 * It provides methods to perform CRUD operations and custom queries on the Token table in the database.
 * It extends JpaRepository, which provides JPA related methods such as save, delete, and find.
 * It is annotated with @Repository to indicate that it's a bean and to translate any platform-specific exceptions into Spring's DataAccessException hierarchy.
 * It includes methods to find a token by its value, find a valid token by its value, find valid tokens by user id, delete expired tokens, and find a token by its value and type.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    /**
     * Find a token by its value.
     *
     * @param token the value of the token.
     * @return an Optional of Token if a token with the given value exists, empty Optional otherwise.
     */
    @Query("select token from Token token where token.value = :token")
    Optional<Token> findByToken(String token);

    /**
     * Find a valid token by its value.
     *
     * @param token the value of the token.
     * @return an Optional of Token if a valid token with the given value exists, empty Optional otherwise.
     */
    @Query("select token from Token token where token.value = :token and token.expired = false")
    Optional<Token> findByTokenValid(String token);

    /**
     * Find valid tokens by user id.
     *
     * @param id the id of the user.
     * @return a List of Token if valid tokens for the given user id exist, empty List otherwise.
     */
    @Query("select t from Token t where t.user.id = ?1 and t.expired = false")
    List<Token> findValidTokensByUserId(Integer id);

    /**
     * Delete expired tokens.
     */
    @Modifying
    @Transactional
    void deleteByExpiredTrue();

    /**
     * Find a token by its value and type.
     *
     * @param value the value of the token.
     * @param type  the type of the token.
     * @return an Optional of Token if a token with the given value and type exists, empty Optional otherwise.
     */
    @Query("select t from Token t where t.value = ?1 and t.type = ?2")
    Optional<Token> findByValueAndType(@NonNull String value, @NonNull TokenType type);

}