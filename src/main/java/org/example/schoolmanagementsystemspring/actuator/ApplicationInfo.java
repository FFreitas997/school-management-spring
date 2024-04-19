package org.example.schoolmanagementsystemspring.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Service;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Service
@RequiredArgsConstructor
public class ApplicationInfo implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("Application Name", "School Management System")
                .withDetail("Description", "This is a simple school management system")
                .withDetail("Version", "1.0.0")
                .withDetail("Author", "Francisco Freitas")
                .withDetail("LinkedIn", "https://www.linkedin.com/in/francisco-freitas-a289b91b3/")
                .withDetail("Github", "https://github.com/FFreitas997");
    }
}
