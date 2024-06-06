package org.example.schoolmanagementsystemspring.admin.aspect;

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
public class AdminServiceMeasure {

    @Around("execution(* org.example.schoolmanagementsystemspring.admin.service.AdminService.*(..))")
    public Object aroundMeasureTimeAdminService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Admin Service has just started: {}", proceedingJoinPoint.getSignature().toShortString());

        log.info("Start measuring the time ...");

        Instant start = Instant.now();

        Object result;

        try {

            result = proceedingJoinPoint.proceed();

        } catch (Throwable throwable) {
            log.error("An error occurred: {}", throwable.getMessage());
            throw throwable;
        } finally {
            Instant end = Instant.now();
            log.info("End measuring the time ...");
            log.info("Execution time: {}", Duration.between(start, end));
        }

        return result;
    }
}
