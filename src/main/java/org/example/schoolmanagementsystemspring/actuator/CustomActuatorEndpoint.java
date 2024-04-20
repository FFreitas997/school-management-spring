package org.example.schoolmanagementsystemspring.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Endpoint(id = "custom-endpoint")
@Component
public class CustomActuatorEndpoint {

    // username is the request parameter for this actuator endpoint
    @ReadOperation
    public String customEndpoint(String username) {
        return "Custom Actuator Endpoint";
    }

    @WriteOperation
    public String writeOperation() {
        return "Write Operation";
    }
}
