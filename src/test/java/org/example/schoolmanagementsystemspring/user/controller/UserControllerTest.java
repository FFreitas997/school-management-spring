package org.example.schoolmanagementsystemspring.user.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private Integer port;

    @Autowired
    UserRepository userRepository;


    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @Test
    void connectionEstablished() {
        assertTrue(container.isCreated());
        assertTrue(container.isRunning());
        assertTrue(container.isHealthy());
    }

    @Test
    void getAllUsers() {
        List<User> users = buildTwoUsers();
        userRepository.saveAll(users);


        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/admin/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    /*@Test
    void getUserById() {
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }*/

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    private List<User> buildTwoUsers(){
        return List.of(
                User
                        .builder()
                        .firstName("Francisco")
                        .lastName("Freitas")
                        .email("francisco@gmail.com")
                        .password("password")
                        .role(Role.ADMIN)
                        .isLocked(false)
                        .isEnabled(true)
                        .expirationDate(LocalDateTime.now().plusYears(1))
                        .build(),
                User
                        .builder()
                        .firstName("Francisco")
                        .lastName("Fernando")
                        .email("francisco.fernando@gmail.com")
                        .password("password123")
                        .role(Role.STUDENT)
                        .isLocked(false)
                        .isEnabled(true)
                        .expirationDate(LocalDateTime.now().plusYears(1))
                        .build()
        );
    }
}