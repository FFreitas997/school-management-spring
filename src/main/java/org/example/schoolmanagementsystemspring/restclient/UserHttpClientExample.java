package org.example.schoolmanagementsystemspring.restclient;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public interface UserHttpClientExample {

    @GetExchange("/users")
    List<Object> findAll();

    @GetExchange("/users/{id}")
    Object findById(@PathVariable int id);
}
