package org.example.schoolmanagementsystemspring.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    @Query("select token from Token token where token.tokenValue = :token")
    Optional<Token> findByToken(String token);

    @Query("select token from Token token where token.tokenValue = :token and token.expired = false and token.revoked = false")
    Optional<Token> findByTokenValid(String token);

    @Query("select token " +
            "from Token token " +
            "inner join User user on token.user.id = user.id " +
            "where user.id = :userID and (token.expired = false or token.revoked = false)")
    List<Token> findAllValidTokensByUserId(Integer userID);
}
