package org.example.schoolmanagementsystemspring.user.service;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserResponseDto;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.example.schoolmanagementsystemspring.user.utils.UserUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size, String sort, String order) {
        Sort.Direction direction = order
                .equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        if (size <= 0) size = 10;
        if (page < 0) page = 0;
        if (sort == null || sort.isEmpty()) sort = "firstName";
        PageRequest pageRequest = PageRequest.of(page, size, direction, sort);
        return repository
                .findAll(pageRequest)
                .map(UserUtility::convertToDto);
    }

    @Override
    public UserResponseDto getUserById(Integer userID) throws UserNotFoundException {
        User response = repository
                .findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
        return UserUtility.convertToDto(response);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto user) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        User model = UserUtility.convertToEntity(user, encoder);
        model.setCreatedAt(LocalDateTime.now());
        model.setCreatedBy(principal.getName());
        User response = repository.save(model);
        return UserUtility.convertToDto(response);
    }

    @Override
    public UserResponseDto updateUser(Integer userID, UserRequestDto user) throws UserNotFoundException {
        if (userID == null || user == null) {
            throw new IllegalArgumentException("Missing user ID or information to update");
        }
        User model = repository
                .findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
        model.setFirstName(user.firstName());
        model.setLastName(user.lastName());
        model.setEmail(user.email());
        model.setPassword(encoder.encode(user.password()));
        model.setUpdatedAt(LocalDateTime.now());
        model.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        User response = repository.save(model);
        return UserUtility.convertToDto(response);
    }

    @Override
    public void deleteUser(Integer userID) {
        repository.deleteById(userID);
    }
}
