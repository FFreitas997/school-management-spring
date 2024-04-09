package org.example.schoolmanagementsystemspring.user.service;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
