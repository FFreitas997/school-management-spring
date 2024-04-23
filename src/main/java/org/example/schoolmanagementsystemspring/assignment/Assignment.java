package org.example.schoolmanagementsystemspring.assignment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "assignment")
@EntityListeners(AuditingEntityListener.class)
public class Assignment {

    @EmbeddedId
    private AssignmentID id;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "feedback", length = 300)
    private String feedback;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "points")
    private Integer points;

    @Column(name = "submission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubmissionType submissionType;

    @Column(name = "assignment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssignmentType assignmentType;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", insertable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "last_modified_by", insertable = false)
    @LastModifiedBy
    private String lastModifiedBy;

}
