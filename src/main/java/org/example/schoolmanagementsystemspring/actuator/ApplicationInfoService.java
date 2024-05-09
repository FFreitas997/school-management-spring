package org.example.schoolmanagementsystemspring.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Service;

/**
 * This service class is responsible for providing application information.
 * It implements the InfoContributor interface from Spring Boot Actuator to contribute
 * information about the application to the /info endpoint.
 */
@Service
@RequiredArgsConstructor
public class ApplicationInfoService implements InfoContributor {

    /**
     * This method is overridden from the InfoContributor interface.
     * It contributes additional details about the application to the /info endpoint.
     *
     * @param builder an Info.Builder object provided by Spring Boot Actuator to build the info details.
     * @author FFreitas
     * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
     * <a href="https://github.com/FFreitas997/">Github</a>
     */
    @Override
    public void contribute(Info.Builder builder) {
        // Adding the application name to the info details
        builder.withDetail("Application Name", "School Management System")
                // Adding a description of the application to the info details
                .withDetail("Description", "This is a simple school management system")
                // Adding the version of the application to the info details
                .withDetail("Version", "1.0.0")
                // Adding the author's name to the info details
                .withDetail("Author", "Francisco Freitas")
                // Adding the author's LinkedIn profile to the info details
                .withDetail("LinkedIn", "https://www.linkedin.com/in/francisco-freitas-a289b91b3/")
                // Adding the author's GitHub profile to the info details
                .withDetail("Github", "https://github.com/FFreitas997");
    }
}
