package org.example.schoolmanagementsystemspring.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface  {


    Future<Void> uploadFile(MultipartFile file);

    byte[] downloadFile(String path, String fileName);
}
