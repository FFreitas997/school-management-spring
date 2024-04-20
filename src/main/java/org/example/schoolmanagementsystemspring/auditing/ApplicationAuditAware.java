package org.example.schoolmanagementsystemspring.auditing;

import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if(auth == null || !auth.isAuthenticated())
            return Optional.empty();
        if (auth.getPrincipal().equals("anonymousUser"))
            return Optional.of("system");
        return Optional
                .ofNullable(((User) auth.getPrincipal()).getUsername());
    }
}
