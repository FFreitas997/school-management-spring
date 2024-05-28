package org.example.schoolmanagementsystemspring.student.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class StudentServiceMeasure {

    @Around("execution(* org.example.schoolmanagementsystemspring.student.service.StudentService.*(..))")
    public Object aroundMeasureTimeStudentService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("Student Service has just started: {}", proceedingJoinPoint.getSignature().toShortString());

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
