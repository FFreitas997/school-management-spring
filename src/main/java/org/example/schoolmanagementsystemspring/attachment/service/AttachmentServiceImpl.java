package org.example.schoolmanagementsystemspring.attachment.service;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.attachment.dto.ResponseAttachmentDto;
import org.example.schoolmanagementsystemspring.attachment.entity.Attachment;
import org.example.schoolmanagementsystemspring.attachment.repository.AttachmentRepository;
import org.example.schoolmanagementsystemspring.attachment.util.AttachmentUtility;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
public class AttachmentServiceImpl implements AttachmentService{

    private final AttachmentRepository repository;
    private final UserRepository userRepository;

    @Override
    public ResponseAttachmentDto createAttachment(MultipartFile file, boolean isProfilePicture) throws IOException {
        if (file == null) throw new IllegalArgumentException("File is required");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findByEmailValid(authentication.getName())
                .orElseThrow(() -> new SecurityException("The user is not authenticated or something is wrong with security context."));
        if (isProfilePicture) {
            Attachment attachment = repository
                    .findByProfilePictureTrue()
                    .orElse(null);
            if (attachment != null) {
                attachment.setData(file.getBytes());
                attachment.setFileName(file.getOriginalFilename());
                attachment.setFileType(file.getContentType());
            }else {
                attachment = AttachmentUtility.buildEntity(file, true, user);
            }
            Attachment savedAttachment = repository.save(attachment);
            return AttachmentUtility.buildDto(savedAttachment);
        }
        Attachment attch = AttachmentUtility.buildEntity(file, true, user);
        Attachment savedAttachment = repository.save(attch);
        return AttachmentUtility.buildDto(savedAttachment);
    }

    @Override
    public List<ResponseAttachmentDto> getAttachmentsByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return repository.findAttachmentByUserEmail(authentication.getName())
                .stream()
                .map(AttachmentUtility::buildDto)
                .toList();
    }

    @Override
    public ResponseAttachmentDto getAttachmentById(int id) {
        return repository
                .findById(id)
                .map(AttachmentUtility::buildDto)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found with ID: " + id));
    }

    @Override
    public ResponseAttachmentDto getProfilePicture() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return repository.findAttachmentByUserEmailAndByProfile(authentication.getName())
                .stream()
                .map(AttachmentUtility::buildDto)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Profile picture not found for user: " + authentication.getName()));
    }

    @Override
    public Attachment getAttachmentEntityById(int id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found with ID: " + id));
    }


}
