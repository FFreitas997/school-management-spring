package org.example.schoolmanagementsystemspring.user.service;

import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The UserService interface defines the operations that a user service must implement.
 * It provides methods for getting all users, getting a user by ID, creating a user, updating a user, deleting a user, uploading a user image, and downloading a user image.
 * It uses the UserDto and UserRequestDto classes to represent users and user requests, respectively.
 * It uses the Page class to represent a page of users.
 * It uses the Authentication class to represent user authentication.
 * It uses the MultipartFile class to represent a user image.
 * It uses the Resource class to represent a user image resource.
 * It throws a UserNotFoundException if a user is not found.
 * It throws an IOException if an I/O error occurs.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface UserService {

    /**
     * The getAllUsers method gets all users.
     * It takes a page number and a page size as parameters.
     * It returns a Page of UserDto objects.
     *
     * @param page the page number.
     * @param size the page size.
     * @return a Page of UserDto objects.
     */
    Page<UserDto> getAllUsers(int page, int size);

    /**
     * The getUserById method gets a user by ID.
     * It takes a user ID as a parameter.
     * It returns a UserDto object.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param userID the ID of the user.
     * @return a UserDto object.
     * @throws UserNotFoundException if the user is not found.
     */
    UserDto getUserById(Integer userID) throws UserNotFoundException;

    /**
     * The createUser method creates a user.
     * It takes a UserRequestDto object as a parameter.
     * It returns a UserDto object.
     *
     * @param user the UserRequestDto object that contains the user data.
     * @return a UserDto object.
     */
    UserDto createUser(UserRequestDto user);

    /**
     * The updateUser method updates a user.
     * It takes a user ID and a UserRequestDto object as parameters.
     * It returns a UserDto object.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param userID the ID of the user.
     * @param user   the UserRequestDto object that contains the new user data.
     * @return a UserDto object.
     * @throws UserNotFoundException if the user is not found.
     */
    UserDto updateUser(Integer userID, UserRequestDto user) throws UserNotFoundException;

    /**
     * The deleteUser method deletes a user.
     * It takes a user ID as a parameter.
     *
     * @param userID the ID of the user.
     */
    void deleteUser(Integer userID) throws UserNotFoundException;

    /**
     * The uploadUserImage method uploads a user image.
     * It takes an Authentication object and a MultipartFile object as parameters.
     * It throws an IOException if an I/O error occurs.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param auth the Authentication object that contains the user authentication.
     * @param file the MultipartFile object that contains the user image.
     * @throws IOException           if an I/O error occurs.
     * @throws UserNotFoundException if the user is not found.
     */
    void uploadUserImage(Authentication auth, MultipartFile file) throws IOException, UserNotFoundException;

    /**
     * The downloadUserImage method downloads a user image.
     * It takes an Authentication object as a parameter.
     * It returns a Resource object that represents the user image.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param auth the Authentication object that contains the user authentication.
     * @return a Resource object that represents the user image.
     * @throws UserNotFoundException if the user is not found.
     */
    Resource downloadUserImage(Authentication auth) throws UserNotFoundException;

    Role getUserRole(Authentication auth) throws UserNotFoundException;
}