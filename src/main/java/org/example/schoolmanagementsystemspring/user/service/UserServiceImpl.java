package org.example.schoolmanagementsystemspring.user.service;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.mapper.UserDTOMapper;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    private final UserDTOMapper userDTOMapper;

    @Override
    public Page<UserDto> getAllUsers(int page, int size, String sort, String order) {
        var direction = order.trim().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        if (size <= 0) size = 10;
        if (page < 0) page = 0;
        if (sort == null || sort.isEmpty()) sort = "firstName";
        PageRequest pageRequest = PageRequest.of(page, size, direction, sort);
        return repository
                .findAll(pageRequest)
                .map(userDTOMapper);
    }

    @Override
    public UserDto getUserById(Integer userID) throws UserNotFoundException {
        User response = repository
                .findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
        return userDTOMapper.apply(response);
    }

    @Override
    public UserDto createUser(UserRequestDto user) {
        User newUser = User
                .builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .password(encoder.encode(user.password()))
                .role(user.role())
                .isLocked(false)
                .isEnabled(true)
                .expirationDate(LocalDateTime.now().plusYears(1))
                .build();
        User response = repository.save(newUser);
        return userDTOMapper.apply(response);
    }

    @Override
    public UserDto updateUser(Integer userID, UserRequestDto user) throws UserNotFoundException {
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
        User response = repository.save(model);
        return userDTOMapper.apply(response);
    }

    @Override
    public void deleteUser(Integer userID) {
        repository.deleteById(userID);
    }
}
