package org.example.schoolmanagementsystemspring.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.repository.TokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 23 L * ?")
    public void deleteExpiredTokens() {
        log.info("Deleting expired and revoked tokens");
        tokenRepository.deleteByExpiredTrue();
    }
}
