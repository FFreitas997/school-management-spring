package org.example.schoolmanagementsystemspring.restclient;


import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Component
public class InterceptorExample implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest req, @NonNull byte[] body, ClientHttpRequestExecution execution) throws IOException {
        /*
        You can use an interceptor to intercept the request do something's like:
            Handle tokens (JWT) in every request
            Do logs for every step in process of the request
            Handle exceptions
            Handle request and response
            Do retries
         */

        return execution.execute(req, body);
    }
}
