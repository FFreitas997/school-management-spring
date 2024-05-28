package org.example.schoolmanagementsystemspring.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.storage.StorageDirectory;
import org.example.schoolmanagementsystemspring.storage.StorageService;
import org.example.schoolmanagementsystemspring.user.dto.UserRequestDto;
import org.example.schoolmanagementsystemspring.user.dto.UserDto;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.mapper.UserDTOMapper;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * The UserServiceImpl class is a service that implements the UserService interface.
 * It uses the UserRepository, PasswordEncoder, UserDTOMapper, and StorageService to perform its operations.
 * It provides methods for getting all users, getting a user by ID, creating a user, updating a user, deleting a user, uploading a user image, and downloading a user image.
 * It uses the @Service, @Transactional, and @RequiredArgsConstructor annotations to define its behavior.
 * It uses the log object to log information about its operations.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserDTOMapper userDTOMapper;
    private final StorageService storageService;

    /**
     * The getAllUsers method gets all users.
     * It takes a page number and a page size as parameters.
     * It returns a Page of UserDto objects.
     * It logs the operation.
     *
     * @param page the page number.
     * @param size the page size.
     * @return a Page of UserDto objects.
     */
    @Override
    public Page<UserDto> getAllUsers(int page, int size) {
        if (size <= 0) size = 10;
        if (page < 0) page = 0;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "firstName");
        return repository
                .findAll(pageRequest)
                .map(userDTOMapper);
    }

    /**
     * The getUserById method gets a user by ID.
     * It takes a user ID as a parameter.
     * It returns a UserDto object.
     * It logs the operation.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param userID the ID of the user.
     * @return a UserDto object.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public UserDto getUserById(Integer userID) throws UserNotFoundException {
        User response = repository
                .findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
        return userDTOMapper.apply(response);
    }

    /**
     * The createUser method creates a user.
     * It takes a UserRequestDto object as a parameter.
     * It returns a UserDto object.
     * It logs the operation.
     *
     * @param user the UserRequestDto object that contains the user data.
     * @return a UserDto object.
     */
    @Override
    public UserDto createUser(UserRequestDto user) {
        User newUser = User
                .builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .password(encoder.encode(user.password()))
                .role(user.role())
                .isEnabled(true)
                .build();
        User response = repository.save(newUser);
        return userDTOMapper.apply(response);
    }

    /**
     * The updateUser method updates a user.
     * It takes a user ID and a UserRequestDto object as parameters.
     * It returns a UserDto object.
     * It logs the operation.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param userID the ID of the user.
     * @param user the UserRequestDto object that contains the new user data.
     * @return a UserDto object.
     * @throws UserNotFoundException if the user is not found.
     */
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

    /**
     * The deleteUser method deletes a user.
     * It takes a user ID as a parameter.
     * It logs the operation.
     *
     * @param userID the ID of the user.
     */
    @Override
    public void deleteUser(Integer userID) throws UserNotFoundException {
        User response = repository
                .findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
        response.setEnabled(false);
        repository.save(response);
    }

    /**
     * The uploadUserImage method uploads a user image.
     * It takes an Authentication object and a MultipartFile object as parameters.
     * It logs the operation.
     * It throws an IOException if an I/O error occurs.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param auth the Authentication object that contains the user authentication.
     * @param file the MultipartFile object that contains the user image.
     * @throws IOException if an I/O error occurs.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public void uploadUserImage(Authentication auth, MultipartFile file) throws IOException, UserNotFoundException {
        if (file == null || file.isEmpty()) {
            log.error("Failed to store empty or null file");
            throw new IOException("Failed to store empty file");
        }

        if (!getFileExtension(Objects.requireNonNull(file.getOriginalFilename())).equals("png")){
            log.error("Failed to store file with invalid extension");
            throw new IOException("Failed to store file with invalid extension. Only PNG files are allowed.");
        }

        User user = repository.findByEmailValid(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + auth.getName()));

        String fileName = buildFileName(user, file);

        user.setProfileImage(fileName);
        repository.save(user);

        storageService.store(fileName, file.getBytes(), StorageDirectory.PROFILE_IMAGE).handle(this::handleStoreImage);
    }

    /**
     * The downloadUserImage method downloads a user image.
     * It takes an Authentication object as a parameter.
     * It returns a Resource object that represents the user image.
     * It logs the operation.
     * It throws a UserNotFoundException if the user is not found.
     *
     * @param auth the Authentication object that contains the user authentication.
     * @return a Resource object that represents the user image.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public Resource downloadUserImage(Authentication auth) throws UserNotFoundException {
        User user = repository.findByEmailValid(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + auth.getName()));

        var resource = storageService.loadResource(user.getProfileImage(), StorageDirectory.PROFILE_IMAGE);

        if (resource == null || !resource.exists() || !resource.isReadable()) {
            log.error("Could not read file: {}", user.getProfileImage());
            throw new IllegalArgumentException("Could not read file: " + user.getProfileImage());
        }

        return resource;
    }

    private String getFileExtension(@NonNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private String getFileName(@NonNull String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private <S> S handleStoreImage(S result, Throwable throwable) {
        if (throwable != null)
            log.error("Error storing image: {}", throwable.getMessage());
        return result;
    }

    private String buildFileName(User user, MultipartFile file) {
        return user.getFirstName() + "_" +
                user.getLastName() + "_" +
                getFileName(Objects.requireNonNull(file.getOriginalFilename())) + "_" +
                System.currentTimeMillis() + "." +
                getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
    }
}
