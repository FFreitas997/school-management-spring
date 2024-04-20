package org.example.schoolmanagementsystemspring.attachment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.attachment.dto.ResponseAttachmentDto;
import org.example.schoolmanagementsystemspring.attachment.entity.Attachment;
import org.example.schoolmanagementsystemspring.attachment.service.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@RestController
@RequestMapping("/api/v1/user/attachments")
@RequiredArgsConstructor
@Tag(name = "Attachments Management System", description = "Endpoints for managing attachments in the system.")
@SecurityRequirement(name = "JSON Web Token (JWT)")
@Slf4j
public class AttachmentController {

    private final AttachmentService service;

    @Operation(
            summary = "Create attachment",
            description = "Create attachment in the system.",
            tags = {"Attachments Management System"}
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseAttachmentDto createAttachment(@RequestParam MultipartFile file, @RequestParam boolean isProfilePicture) throws IOException {
        log.info("Creating attachment in the system.");
        ResponseAttachmentDto response = service.createAttachment(file, isProfilePicture);
        response.setDownloadURL(buildDownloadURL(response));
        return response;
    }

    @Operation(
            summary = "Get attachments for current user",
            description = "Get attachments for current user in the system.",
            tags = {"Attachments Management System"}
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseAttachmentDto> getAttachmentsByCurrentUser() {
        log.info("Getting attachments for current user in the system.");
        List<ResponseAttachmentDto> response = service.getAttachmentsByCurrentUser();
        response.forEach(r -> r.setDownloadURL(buildDownloadURL(r)));
        return response;
    }

    @Operation(
            summary = "Get attachment by ID",
            description = "Get attachment by ID in the system.",
            tags = {"Attachments Management System"}
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseAttachmentDto getAttachmentById(@PathVariable int id) {
        log.info("Getting attachment by ID in the system.");
        ResponseAttachmentDto response = service.getAttachmentById(id);
        response.setDownloadURL(buildDownloadURL(response));
        return response;
    }

    @Operation(
            summary = "Get attachment by ID",
            description = "Get attachment by ID in the system.",
            tags = {"Attachments Management System"}
    )
    @GetMapping("/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    public ResponseAttachmentDto getAttachmentPictureCurrentUser() {
        log.info("Getting profile picture for current user in the system.");
        ResponseAttachmentDto response = service.getProfilePicture();
        response.setDownloadURL(buildDownloadURL(response));
        return response;
    }

    private String buildDownloadURL(ResponseAttachmentDto response) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(response.getId() + "")
                .toUriString();
    }

    // build download endpoint in the controller
    @Operation(
            summary = "Download attachment by ID",
            description = "Download attachment by ID in the system.",
            tags = {"Attachments Management System"}
    )
    @GetMapping("/download/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> downloadAttachmentById(@PathVariable int id) {
        log.info("Downloading attachment by ID in the system.");
        Attachment attachment = service.getAttachmentEntityById(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }
}
