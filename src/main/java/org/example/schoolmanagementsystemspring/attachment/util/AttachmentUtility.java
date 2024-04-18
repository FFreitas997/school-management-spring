package org.example.schoolmanagementsystemspring.attachment.util;

import org.example.schoolmanagementsystemspring.attachment.dto.ResponseAttachmentDto;
import org.example.schoolmanagementsystemspring.attachment.entity.Attachment;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
public final class AttachmentUtility {

    private AttachmentUtility() {}

    public static Attachment buildEntity(MultipartFile file, boolean isProfilePicture, User user) throws IOException {
        return Attachment
                .builder()
                .fileType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .data(file.getBytes())
                .profilePicture(isProfilePicture)
                .user(user)
                .build();
    }

    public static ResponseAttachmentDto buildDto(Attachment attachment) {
        return ResponseAttachmentDto
                .builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .build();
    }
}
