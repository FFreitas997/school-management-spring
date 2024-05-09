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
 * The UserServiceAspect class is an aspect that measures the execution time of the methods in the UserService class.
 * It uses the @Around annotation to wrap around the UserService methods and measure their execution time.
 * It logs the start and end time of the method execution, as well as the total execution time in milliseconds.
 * If an exception occurs during the execution of the UserService method, it logs the exception message and rethrows the exception.
 * If the result of the method execution is a Page object, it logs the total number of elements and pages.
 * If the user is not authenticated, it logs an error message and throws a SecurityException.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserServiceAspect {

    /**
     * The aroundMeasureTimeUserService method measures the execution time of the methods in the UserService class.
     * It logs the start and end time of the method execution, as well as the total execution time in milliseconds.
     * If an exception occurs during the execution of the UserService method, it logs the exception message and rethrows the exception.
     * If the result of the method execution is a Page object, it logs the total number of elements and pages.
     * If the user is not authenticated, it logs an error message and throws a SecurityException.
     *
     * @param proceedingJoinPoint the ProceedingJoinPoint object that allows the method to proceed with its original instructions.
     * @return the result of the UserService method execution.
     * @throws Throwable if an exception occurs during the execution of the UserService method.
     */
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
            log.info("Duration: {} milliseconds", Duration.between(start, endTime).toMillis());
        }

        if (result instanceof Page<?> page) {
            log.info("Total Elements: {}", page.getTotalElements());
            log.info("Total Pages: {}", page.getTotalPages());
        }

        return result;
    }

}
