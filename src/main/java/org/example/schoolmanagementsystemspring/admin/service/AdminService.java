package org.example.schoolmanagementsystemspring.admin.service;

import org.example.schoolmanagementsystemspring.admin.dto.AdminRequestRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;

/**
 * The AdminService interface provides the contract for the admin service.
 * It declares a method for registering a new admin user.
 * Implementations of this interface are expected to provide the business logic for admin operations.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface AdminService {

    /**
     * The register method is responsible for registering a new admin user.
     * It accepts an AdminRequestRegisterDTO object which contains the details of the new admin user.
     * It throws a UserAlreadyExistsException if a user with the same email already exists.
     * It throws a UserNotFoundException if the user is not found in the database.
     *
     * @param request a valid AdminRequestRegisterDTO object containing the details of the new admin user.
     * @throws UserAlreadyExistsException if a user with the same email already exists.
     * @throws UserNotFoundException if the user is not found in the database.
     */
    void register(AdminRequestRegisterDTO request) throws UserAlreadyExistsException, UserNotFoundException;

    // TODO: associate teacher to a course or more than one course
    // Create School and management
}
