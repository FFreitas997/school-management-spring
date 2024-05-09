package org.example.schoolmanagementsystemspring.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileManagementServiceImpl implements FileManagementService{


    @Override
    public Future<Void> uploadFile(MultipartFile file) {

    }

    @Override
    public byte[] downloadFile(String path, String fileName) {
        return new byte[0];
    }
}
