package org.example.schoolmanagementsystemspring.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@OpenAPIDefinition(
        info = @Info(
                title = "School Management System",
                version = "1.0",
                description = "API for School Management System",
                contact = @Contact(
                        name = "Francisco Freitas",
                        email = "francisco.freitas.ff@gmail.com",
                        url = "https://www.linkedin.com/in/francisco-freitas-a289b91b3"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(
                        description = "Local Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Server",
                        url = "https://school-management-system-spring.herokuapp.com"
                )
        },
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Documentation",
                url = "https://spring.io/projects/spring-boot"
        )
)
@SecurityScheme(
        name = "JSON Web Token (JWT)",
        description = "JSON Web Token (JWT) is an open standard (RFC 7519) that " +
                "defines a compact and self-contained way for securely transmitting information " +
                "between parties as a JSON object. This information can be verified and trusted " +
                "because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm)" +
                " or a public/private key pair using RSA or ECDSA.",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
