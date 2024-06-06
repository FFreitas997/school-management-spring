package org.example.schoolmanagementsystemspring.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.repository.TokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The ScheduledTasksService class is a service that handles scheduled tasks.
 * It uses the TokenRepository to perform its operations.
 * It provides a method for deleting expired tokens, which is scheduled to run at the end of every month.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private final TokenRepository tokenRepository;

    /**
     * The deleteExpiredTokens method deleted expired tokens.
     * It is scheduled to run at the end of every month.
     * It logs the start of the operation and then calls the deleteByExpiredTrue method of the TokenRepository
     * to delete the expired tokens.
     */
    @Scheduled(cron = "0 0 23 L * ?")
    public void deleteExpiredTokens() {
        log.info("Deleting expired tokens");
        tokenRepository.deleteByExpiredTrue();
    }
}