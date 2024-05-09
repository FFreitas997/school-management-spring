package org.example.schoolmanagementsystemspring.storage;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The StorageServiceImpl class is a service that handles the storage of profile images.
 * It uses the StorageService interface to define its operations.
 * It provides methods for storing, loading, and deleting profile images.
 * It uses the @Value annotation to inject the profile directory from the application properties.
 * It uses the @PostConstruct annotation to initialize the root location of the profile images.
 * It uses the @Async annotation to perform the storeProfileImage operation asynchronously.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Value("${storage.profile-directory}")
    private String profileDirectory;

    private Path rootLocation;

    /**
     * The init method initializes the root location of the profile images.
     * It is called after the construction of the StorageServiceImpl object.
     */
    @PostConstruct
    public void init() {
        log.info("Storage Service initialized ...");
        rootLocation = Paths.get(profileDirectory);
    }

    /**
     * The storeProfileImage method stores a profile image.
     * It checks if the content is empty, resolves the file path, checks if the file is within the current directory,
     * creates the directory for the file if it doesn't exist, and writes the content to the file.
     * If an error occurs, it logs the error and returns a failed future.
     * Otherwise, it returns a completed future with the value null.
     *
     * @param fileName the name of the file to be stored.
     * @param content  the content of the file to be stored.
     * @return a CompletableFuture indicating the result of the file storing operation.
     */
    @Override
    @Async
    public CompletableFuture<Void> storeProfileImage(@NonNull String fileName, @NonNull byte[] content) {
        try {

            log.info("Storing file: {}", fileName);

            // Check if the content is empty
            if (content.length == 0) {
                log.error("Failed to store empty file");
                throw new StorageException("Failed to store empty file");
            }

            // Resolve the file path and normalize it to an absolute path
            Path destinationFile = rootLocation.resolve(Paths.get(Objects.requireNonNull(fileName)))
                    .normalize().toAbsolutePath();

            // Check if the file is within the current directory
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                log.error("Cannot store file outside current directory");
                throw new StorageException("Cannot store file outside current directory");
            }

            // If the directory for the file doesn't exist, create it
            Files.createDirectories(destinationFile.getParent());

            // Write the content to the file
            Files.write(destinationFile, content, StandardOpenOption.CREATE);

            log.info("File stored: {}", fileName);

            // Return a CompletableFuture that is completed when the file is stored
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Error storing file: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * The loadProfileImageResource method loads a profile image resource.
     * It resolves the file path, checks if the resource exists or is readable, and returns the resource.
     * If an error occurs, it logs the error and throws a StorageException.
     *
     * @param fileName the name of the file to be loaded.
     * @return a Resource object representing the loaded profile image resource.
     * @throws StorageException if an error occurs while loading the file.
     */
    @Override
    public Resource loadProfileImageResource(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName)
                    .normalize().toAbsolutePath();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("Could not read file: {}", fileName);
                throw new StorageException("Could not read file: " + fileName);
            }
        } catch (Exception e) {
            log.error("Error loading file: {}", e.getMessage());
            throw new StorageException("Error loading file: " + e.getMessage());
        }
    }

    /**
     * The delete method deletes a profile image.
     * It resolves the file path and deletes the file.
     *
     * @param fileName the name of the file to be deleted.
     */
    @Override
    public void delete(String fileName) {
        FileSystemUtils.deleteRecursively(rootLocation.resolve(fileName).toFile());
    }
}
