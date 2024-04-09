package org.example.schoolmanagementsystemspring.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Configuration
public class RestClientConfiguration {

    @Bean
    public UserHttpClientExample userHttpClientExample(InterceptorExample interceptor) {
        RestClient restClient = RestClient
                .builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .requestInterceptor(interceptor)
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(UserHttpClientExample.class);
    }
}
