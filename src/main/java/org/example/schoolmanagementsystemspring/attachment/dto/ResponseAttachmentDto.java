package org.example.schoolmanagementsystemspring.attachment.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link org.example.schoolmanagementsystemspring.attachment.entity.Attachment}
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAttachmentDto implements Serializable{
    private Integer id;
    private String fileName;
    private String fileType;
    private String downloadURL;
}