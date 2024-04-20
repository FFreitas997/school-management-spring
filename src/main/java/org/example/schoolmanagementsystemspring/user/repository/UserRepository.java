package org.example.schoolmanagementsystemspring.user.repository;

import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email = :email and u.isLocked = false and u.isEnabled = true")
    Optional<User> findByEmailValid(String email);
}
