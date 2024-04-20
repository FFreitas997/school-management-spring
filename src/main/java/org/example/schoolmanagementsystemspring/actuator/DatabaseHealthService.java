package org.example.schoolmanagementsystemspring.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Service
@RequiredArgsConstructor
public class DatabaseHealthService implements HealthIndicator {

    @Override
    public Health health() {
        HashMap<String, String> info = new HashMap<>();
        info.put("Example", "This is an example of how to add more information to the health check");
        try {
            info.put("Database Service", "The database service is running");
            return Health.up().withDetails(info).build();
        }catch (Exception e){
            info.put("Error Code", "Database is down");
            return Health.down().withDetails(info).build();
        }
    }
}
