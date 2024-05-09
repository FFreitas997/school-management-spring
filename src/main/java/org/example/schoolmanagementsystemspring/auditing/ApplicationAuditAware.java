package org.example.schoolmanagementsystemspring.auditing;

import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * The ApplicationAuditAware class implements the AuditorAware interface from Spring Data JPA.
 * It is used to provide the current auditor for auditing purposes.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public class ApplicationAuditAware implements AuditorAware<String> {

    /**
     * This method is overridden from the AuditorAware interface.
     * It retrieves the current authenticated user from the SecurityContext.
     * If there is no authenticated user or the user is anonymous, it returns an appropriate value.
     *
     * @return an Optional<String> containing the username of the authenticated user, "system" for anonymous users, or empty if there is no authenticated user.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        // If there is no authenticated user, return an empty Optional
        if (auth == null || !auth.isAuthenticated())
            return Optional.empty();
        // If the user is anonymous, return "system"
        if (auth.getPrincipal().equals("anonymousUser"))
            return Optional.of("system");
        // Otherwise, return the username of the authenticated user
        return Optional
                .ofNullable(((User) auth.getPrincipal()).getUsername());
    }
}
