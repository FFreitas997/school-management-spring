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
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EmailServiceAspect {

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
        }finally {
            Instant endTime = Instant.now();
            log.info("Method Finished the execution");
            log.info("Duration: {} seconds || {} milliseconds",
                    Duration.between(start, endTime).toSeconds(),
                    Duration.between(start, endTime).toMillis()
            );
        }
        return result;
    }
}
