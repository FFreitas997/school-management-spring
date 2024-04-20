package org.example.schoolmanagementsystemspring.attachment.service;

import org.example.schoolmanagementsystemspring.attachment.dto.ResponseAttachmentDto;
import org.example.schoolmanagementsystemspring.attachment.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface AttachmentService {

    ResponseAttachmentDto createAttachment(MultipartFile file, boolean isProfilePicture) throws IOException;

    List<ResponseAttachmentDto> getAttachmentsByCurrentUser();

    ResponseAttachmentDto getAttachmentById(int id);

    ResponseAttachmentDto getProfilePicture();

    Attachment getAttachmentEntityById(int id);
}
