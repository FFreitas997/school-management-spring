package org.example.schoolmanagementsystemspring;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.example.schoolmanagementsystemspring.user.entity.Role.ADMIN;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommandLineRunnerService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger log = LoggerFactory.getLogger(CommandLineRunnerService.class);
    @Value("${app.user.email}")
    private String email;
    @Value("${app.user.password}")
    private String password;
    @Value("${app.user.firstName}")
    private String firstName;
    @Value("${app.user.lastName}")
    private String lastName;

    @Override
    public void run(String... args) {
        User mainUser = userRepository
                .findByEmail(email)
                .orElse(null);
        if (mainUser != null) return;
        log.info("Creating main user: {}", email);
        User saveUser = User
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(ADMIN)
                .expirationDate(LocalDateTime.now().plusYears(10))
                .isEnabled(true)
                .isLocked(false)
                .createdAt(LocalDateTime.now())
                .createdBy("System")
                .build();
        userRepository.save(saveUser);
    }
}
