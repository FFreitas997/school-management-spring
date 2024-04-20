package org.example.schoolmanagementsystemspring.attachment.util;

import org.example.schoolmanagementsystemspring.attachment.dto.ResponseAttachmentDto;
import org.example.schoolmanagementsystemspring.attachment.entity.Attachment;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
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
