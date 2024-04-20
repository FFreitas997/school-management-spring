package org.example.schoolmanagementsystemspring.restclient;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface UserHttpClientExample {

    @GetExchange("/users")
    List<Object> findAll();

    @GetExchange("/users/{id}")
    Object findById(@PathVariable int id);
}
