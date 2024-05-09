package org.example.schoolmanagementsystemspring.authentication.service;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserDetailsServiceImpl class is a service that implements the UserDetailsService interface.
 * It provides a method for loading a user by their email.
 * It uses the UserRepository to perform its operations.
 * If a user with the provided email is not found, it throws a UsernameNotFoundException.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    /**
     * The loadUserByUsername method is overridden to load a user by their email.
     * It retrieves the user from the repository.
     * If a user with the provided email is not found, it throws a UsernameNotFoundException.
     *
     * @param userEmail the email of the user to be loaded.
     * @return a UserDetails object representing the loaded user.
     * @throws UsernameNotFoundException if a user with the provided email is not found.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository
                .findByEmailValid(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }
}