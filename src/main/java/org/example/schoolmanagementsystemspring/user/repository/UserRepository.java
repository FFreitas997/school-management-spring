package org.example.schoolmanagementsystemspring.user.repository;

import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The UserRepository interface extends JpaRepository and provides methods for querying User entities.
 * It provides methods for finding a user by email and finding a valid user by email.
 * It uses the @Repository annotation to indicate that it is a Spring Data repository.
 * It uses the @Query annotation to define custom queries.
 * It uses the User class to represent users.
 * It uses the Optional class to represent optional results.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * The findByEmail method finds a user by email.
     * It takes an email as a parameter.
     * It returns an Optional of User.
     * It uses the @Query annotation to define a custom query.
     *
     * @param email the email of the user.
     * @return an Optional of User.
     */
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);

    /**
     * The findByEmailValid method finds a valid user by email.
     * It takes an email as a parameter.
     * It returns an Optional of User.
     * It uses the @Query annotation to define a custom query.
     *
     * @param email the email of the user.
     * @return an Optional of User.
     */
    @Query("select u from User u where u.email = :email and u.isEnabled = true")
    Optional<User> findByEmailValid(String email);

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(@NonNull String email);
}