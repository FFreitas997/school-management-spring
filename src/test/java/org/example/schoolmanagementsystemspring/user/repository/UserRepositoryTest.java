package org.example.schoolmanagementsystemspring.user.repository;

import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void connectionTest() {
        assertTrue(container.isCreated());
        assertTrue(container.isRunning());
    }

    @Test
    void saveUserTest() {
        User user = createUser();
        User savedUser = userRepository.save(user);
        assertEquals("Francisco", savedUser.getFirstName());
        assertEquals("Freitas", savedUser.getLastName());
        assertEquals("francisco.freitas@gmail.com", savedUser.getEmail());
        assertEquals("password", savedUser.getPassword());
        assertEquals(Role.ADMIN, savedUser.getRole());
        assertTrue(savedUser.getExpirationDate().isAfter(LocalDateTime.now()));
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());
        assertNotEquals(0, savedUser.getAuthorities().size());
    }

    private User createUser() {
        return User
                .builder()
                .firstName("Francisco")
                .lastName("Freitas")
                .email("francisco.freitas@gmail.com")
                .password("password")
                .role(Role.ADMIN)
                .isLocked(false)
                .isEnabled(true)
                .expirationDate(LocalDateTime.now().plusYears(1))
                .build();
    }
}
