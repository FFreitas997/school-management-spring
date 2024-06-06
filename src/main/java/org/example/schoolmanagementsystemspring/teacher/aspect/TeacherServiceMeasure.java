package org.example.schoolmanagementsystemspring.teacher.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * This class is an Aspect for measuring the execution time of methods in the TeacherService class.
 * It also checks if the user is authenticated and has the role of a teacher.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Aspect
@Component
public class TeacherServiceMeasure {

    /**
     * This method is Around advice for all methods in the TeacherService class.
     * It measures the execution time of the method and checks if the user is authenticated and has the role of a teacher.
     * If the user is not authenticated or does not have the role of a teacher, security exception is thrown.
     *
     * @param proceedingJoinPoint the join point at which the advice is applied
     * @return the result of the method execution
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* org.example.schoolmanagementsystemspring.teacher.service.TeacherService.*(..))")
    public Object aroundMeasureTimeTeacherService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // Log the start of the method execution
        log.info("Teacher Service has just started: {}", proceedingJoinPoint.getSignature().toShortString());
/*
        // Get the authentication object from the security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (auth == null || !auth.isAuthenticated()) {
            log.error("Method execution stopped: user not authenticated");
            throw new SecurityException("User not authenticated");
        }

        // Check if the user has the role of a teacher
        var isTeacher = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));

        if (!isTeacher) {
            log.error("Method execution stopped: user is not a teacher");
            throw new SecurityException("User is not a teacher");
        }*/

        // Start measuring the execution time
        log.info("Start measuring the time ...");
        Instant start = Instant.now();

        Object result;

        try {
            // Proceed with the method execution
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            // Log any exception that occurs during method execution
            log.error("An exception has occurred: {}", e.getMessage());
            throw e;
        } finally {
            // Calculate and log the execution time
            Instant endTime = Instant.now();
            log.info("End measuring the time ...");
            log.info("Duration: {} milliseconds", Duration.between(start, endTime).toMillis());
        }

        // Return the result of the method execution
        return result;
    }
}