package org.example.schoolmanagementsystemspring.user.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserServiceAspect {

    @Around("execution(* org.example.schoolmanagementsystemspring.user.service.UserService.*(..))")
    public Object aroundMeasureTimeUserService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("User service method has just started with signature: {}", proceedingJoinPoint.getSignature().toShortString());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            log.error("Method execution stopped: User not authenticated");
            throw new SecurityException("User not authenticated");
        }

        log.info("Start measuring the time...");

        Instant start = Instant.now();

        Object result;

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            log.error("An exception has occurred: {}", e.getMessage());
            throw e;
        }finally {
            Instant endTime = Instant.now();

            log.info("Method Finished the execution");
            log.info("Duration: {} seconds || {} milliseconds",
                    Duration.between(start, endTime).toSeconds(),
                    Duration.between(start, endTime).toMillis()
            );
        }



        if (result instanceof Page<?> page) {
            log.info("Total Elements: {}", page.getTotalElements());
            log.info("Total Pages: {}", page.getTotalPages());
        }

        return result;
    }

}
