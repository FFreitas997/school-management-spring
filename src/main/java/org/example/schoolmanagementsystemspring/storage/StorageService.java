package org.example.schoolmanagementsystemspring.storage;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface StorageService {

    @Async
    CompletableFuture<Void> storeProfileImage(@NonNull String fileName, @NonNull byte[] content);

    Resource loadProfileImageResource(String fileName);

    void delete(String fileName);
}
