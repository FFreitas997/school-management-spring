package org.example.schoolmanagementsystemspring.parent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.schoolmanagementsystemspring.student.entity.Student;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
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
@SuperBuilder
@Entity
@Table(name = "parent")
@PrimaryKeyJoinColumn(name = "parent_id")
@EntityListeners(AuditingEntityListener.class)
public class Parent extends User {

    @Column(name = "phone_number", nullable = false, length = 9)
    private String phoneNumber;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParentType type;

    @Column(name = "occupation", nullable = false)
    private String occupation;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student studentResponsible;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", insertable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
