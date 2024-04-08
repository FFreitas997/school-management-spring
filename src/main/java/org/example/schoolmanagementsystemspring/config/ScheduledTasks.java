package org.example.schoolmanagementsystemspring.config;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.token.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 23 L * ?")
    public void deleteExpiredTokens() {
        log.info("Deleting expired and revoked tokens");
        tokenRepository.deleteByExpiredTrueAndRevokedTrue();
    }
}
