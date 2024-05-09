package org.example.schoolmanagementsystemspring.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

/**
 * The EmailService class is a service that handles the sending of emails.
 * It uses the JavaMailSender to send emails and the SpringTemplateEngine to process the email templates.
 * It provides a method for sending an email asynchronously.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine engine;

    /**
     * The sendEmail method sends an email asynchronously.
     * It creates a MimeMessage, sets the from, to, and subject fields, processes the email template, and sends the email.
     * If an error occurs while sending the email, it logs the error and returns a failed future.
     * Otherwise, it returns a completed future with the value true.
     *
     * @param email the email to be sent.
     * @return a CompletableFuture indicating the result of the email sending operation.
     */
    @Async
    public CompletableFuture<Boolean> sendEmail(@NonNull Email email) {
        log.info("Execution Thread: {}", Thread.currentThread().getName());
        log.info("Sending email to {}", email.to());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED, UTF_8.name());

            helper.setFrom(email.from());
            helper.setTo(email.to());
            helper.setSubject(email.subject());

            Context context = new Context();
            context.setVariables(email.templateProperties());

            String msg = engine.process(email.template().getTemplateName(), context);

            helper.setText(msg, true);

            BiConsumer<String, Resource> handleInlineResource = (key, resource) -> {
                try {
                    if (resource != null && resource.exists())
                        helper.addInline(key, resource);
                } catch (MessagingException e) {
                    log.error("An error occurred while adding the inline resource to the email");
                }
            };

            if (email.resources() != null)
                email.resources().forEach(handleInlineResource);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("An error occurred while sending the email to {}", email.to());
            log.error("Error message: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
        return CompletableFuture.completedFuture(true);
    }
}