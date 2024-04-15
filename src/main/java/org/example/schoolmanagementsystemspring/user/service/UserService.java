package org.example.schoolmanagementsystemspring.user.service;

import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public interface UserService {

    Page<UserDto> getAllUsers(int page, int size, String sort, String order);

    UserDto getUserById(Integer userID) throws UserNotFoundException;

    UserDto createUser(UserRequestDto user);

    UserDto updateUser(Integer userID, UserRequestDto user) throws UserNotFoundException;

    void deleteUser(Integer userID);
}
