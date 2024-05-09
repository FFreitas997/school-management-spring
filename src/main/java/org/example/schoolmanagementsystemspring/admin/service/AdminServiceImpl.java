package org.example.schoolmanagementsystemspring.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.admin.dto.AdminRequestRegisterDTO;
import org.example.schoolmanagementsystemspring.admin.mapper.UserMapper;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The AdminServiceImpl class is a service class that implements the AdminService interface.
 * It provides the business logic for admin operations.
 * It is annotated with @Service to indicate that it's a service class,
 * annotation @RequiredArgsConstructor for automatic generation of a constructor with required arguments,
 * and @Transactional to enable Spring's declarative transaction management.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    /**
     * The UserMapper is used to map an AdminRequestRegisterDTO object to a User object.
     */
    private final UserMapper userMapper;

    /**
     * The AuthenticationService is used to generate an activation code for a new user.
     */
    private final AuthenticationService authenticationService;

    /**
     * The UserRepository is used to interact with the User table in the database.
     */
    private final UserRepository userRepository;

    /**
     * The PasswordEncoder is used to encode the password of a new user.
     */
    private final PasswordEncoder encoder;

    /**
     * The register method handles the registration of a new admin user.
     * It first maps the AdminRequestRegisterDTO object to a User object,
     * encodes the password, checks if the user already exists in the database,
     * saves the new user in the database, and generates an activation code for the new user.
     *
     * @param request a valid AdminRequestRegisterDTO object containing the details of the new admin user.
     * @throws UserAlreadyExistsException if the user already exists.
     * @throws UserNotFoundException      if the user is not found.
     */
    @Override
    public void register(AdminRequestRegisterDTO request) throws UserAlreadyExistsException, UserNotFoundException {
        User userMapped = userMapper.apply(request);

        userMapped.setPassword(encoder.encode(request.password()));

        var checkUser = userRepository.findByEmail(request.email());

        if (checkUser.isPresent())
            throw new UserAlreadyExistsException("User already exists");

        userRepository.save(userMapped);

        authenticationService.generateActivationCode(request.email());
    }
}
