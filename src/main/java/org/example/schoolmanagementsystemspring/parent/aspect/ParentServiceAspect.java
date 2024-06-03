package org.example.schoolmanagementsystemspring.parent.aspect;

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
public class ParentServiceAspect {

    @Around("execution(* org.example.schoolmanagementsystemspring.parent.service.ParentService.*(..))")
    public Object aroundMeasureTimeParentService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("Parent Service has just started: {}", proceedingJoinPoint.getSignature().toShortString());

        log.info("Start measuring the time ...");

        Instant start = Instant.now();

        Object result;

        try {

            result = proceedingJoinPoint.proceed();

        } catch (Throwable e) {

            log.error("An exception has occurred: {}", e.getMessage());

            throw e;

        } finally {

            Instant endTime = Instant.now();

            log.info("End measuring the time ...");

            log.info("Duration: {} milliseconds", Duration.between(start, endTime).toMillis());

        }

        return result;
    }
}
