package org.example.schoolmanagementsystemspring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.schoolmanagementsystemspring.user.entity.Role.ADMIN;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommandLineRunnerService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                .findByEmailValid(email)
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
                .isEnabled(true)
                .build();
        userRepository.save(saveUser);
    }
}
