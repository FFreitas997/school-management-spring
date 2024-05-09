package org.example.schoolmanagementsystemspring.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * The EmailServiceAspect class is an aspect that measures the execution time of the sendEmail method in the EmailService class.
 * It uses the @Around annotation to wrap around the sendEmail method and measure its execution time.
 * It logs the start and end time of the method execution, as well as the total execution time in seconds and milliseconds.
 * If an exception occurs during the execution of the sendEmail method, it logs the exception message and rethrows the exception.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EmailServiceAspect {

    /**
     * The aroundMeasureSendEmail method measures the execution time of the sendEmail method in the EmailService class.
     * It logs the start and end time of the method execution, as well as the total execution time in seconds and milliseconds.
     * If an exception occurs during the execution of the sendEmail method, it logs the exception message and rethrows the exception.
     *
     * @param proceedingJoinPoint the ProceedingJoinPoint object that allows the method to proceed with its original instructions.
     * @return the result of the sendEmail method execution.
     * @throws Throwable if an exception occurs during the execution of the sendEmail method.
     */
    @Around("execution(* org.example.schoolmanagementsystemspring.mail.EmailService.sendEmail(..))")
    public Object aroundMeasureSendEmail(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Email service method has just started with signature: {}", proceedingJoinPoint.getSignature().toShortString());

        log.info("The time measure will start ...");

        Instant start = Instant.now();

        Object result;

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            log.error("An exception has occurred: {}", e.getMessage());
            throw e;
        } finally {
            Instant endTime = Instant.now();
            log.info("Method Finished the execution");
            log.info("Duration: {} milliseconds", Duration.between(start, endTime).toMillis());
        }
        return result;
    }
}