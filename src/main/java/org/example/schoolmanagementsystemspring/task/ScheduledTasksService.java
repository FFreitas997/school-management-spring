package org.example.schoolmanagementsystemspring.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.repository.TokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 23 L * ?")
    public void deleteExpiredTokens() {
        log.info("Deleting expired and revoked tokens");
        tokenRepository.deleteByExpiredTrueAndRevokedTrue();
    }
}
