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
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("select token from Token token where token.value = :token")
    Optional<Token> findByToken(String token);

    @Query("select token from Token token where token.value = :token and token.expired = false and token.revoked = false")
    Optional<Token> findByTokenValid(String token);

    @Query("select token " +
            "from Token token " +
            "inner join User user on token.user.id = user.id " +
            "where user.id = :userID and (token.expired = false or token.revoked = false)")
    List<Token> findAllValidTokensByUserId(Integer userID);

    @Modifying
    @Transactional
    void deleteByExpiredTrueAndRevokedTrue();

    @Query("select t from Token t where t.value = ?1 and t.type = ?2")
    Optional<Token> findByValueAndType(@NonNull String value, @NonNull TokenType type);

}
