package org.example.schoolmanagementsystemspring.teacher;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.schoolmanagementsystemspring.course.Course;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.student.Student;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "teacher")
@PrimaryKeyJoinColumn(name = "teacher_id")
@EntityListeners(AuditingEntityListener.class)
public class Teacher extends User {

    @Column(name = "phone_number", length = 9, nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "education_qualification", length = 300)
    private String educationQualification;

    @Column(name = "work_expirence", length = 300)
    private String experience;

    @Column(name = "recognition", length = 300)
    private String recognition;

    @Column(name = "teachMethod")
    private String teachMethod;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "teacherResponsible", fetch = FetchType.LAZY)
    private List<Student> studentResponsibleFor;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", insertable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
