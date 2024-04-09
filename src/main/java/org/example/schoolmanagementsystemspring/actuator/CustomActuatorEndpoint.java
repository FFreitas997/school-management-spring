package org.example.schoolmanagementsystemspring.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
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
