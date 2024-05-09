package org.example.schoolmanagementsystemspring.authentication.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;


/**
 * This class is responsible for logging and measuring the execution time of methods in the AuthenticationService.
 * It uses Aspect Oriented Programming (AOP) to achieve this.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationServiceAspect {

    /**
     * This method is around advice for all methods in the AuthenticationService.
     * It logs the start of the method execution, measures the execution time, and logs any exceptions that occur.
     *
     * @param proceedingJoinPoint the join point representing the method execution.
     * @return the result of the method execution.
     * @throws Throwable if any exception occurs during the method execution.
     */
    @Around("execution(* org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService.*(..))")
    public Object aroundMeasureTimeAuthenticationService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // Log the start of the method execution
        log.info("Auth service method has just started with signature: {}", proceedingJoinPoint.getSignature().toShortString());

        // Log the start of the time measurement
        log.info("The time measure will start soon ...");

        // Record the start time
        Instant start = Instant.now();

        Object result;

        try {
            // Proceed with the method execution and store the result
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            // Log any exception that occurs
            log.error("An exception has occurred: {}", e.getMessage());
            throw e;
        } finally {
            // Record the end time
            Instant endTime = Instant.now();
            // Log the end of the method execution
            log.info("Method Finished the execution");
            // Log the duration of the method execution
            log.info("Duration: {} milliseconds", Duration.between(start, endTime).toMillis());
        }
        // Return the result of the method execution
        return result;
    }
}
