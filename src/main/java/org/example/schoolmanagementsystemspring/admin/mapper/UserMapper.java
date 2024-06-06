package org.example.schoolmanagementsystemspring.admin.mapper;

import org.example.schoolmanagementsystemspring.admin.dto.RequestUser;
import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * The UserMapper class is a component that implements the Function interface.
 * It provides a method to map an AdminRequestRegisterDTO object to a User object.
 * This is used when registering a new admin user.
 * The apply method takes an AdminRequestRegisterDTO object as input and returns a User object.
 * The User object is built with the details from the AdminRequestRegisterDTO object and some default values.
 * The role is set to ADMIN and isEnabled is set to false.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Component
public class UserMapper implements Function<RequestUser, User> {

    /**
     * The apply method maps an AdminRequestRegisterDTO object to a User object.
     * It sets the first name, last name, and email from the AdminRequestRegisterDTO object.
     * It sets the role to ADMIN and isEnabled to false.
     *
     * @param dto a valid AdminRequestRegisterDTO object containing the details of the new admin user.
     * @return a User object with the details from the AdminRequestRegisterDTO object and some default values.
     */
    @Override
    public User apply(RequestUser dto) {
        return User
                .builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(dto.password())
                .role(dto.role())
                .description(dto.description())
                .isEnabled(false)
                .build();
    }
}