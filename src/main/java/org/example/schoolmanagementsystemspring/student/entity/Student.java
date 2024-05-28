package org.example.schoolmanagementsystemspring.student.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.schoolmanagementsystemspring.course.Course;
import org.example.schoolmanagementsystemspring.parent.Parent;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.teacher.entity.Teacher;
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
@Table(name = "student")
@PrimaryKeyJoinColumn(name = "student_id")
@EntityListeners(AuditingEntityListener.class)
public class Student extends User {

    @Column(name = "student_identification", nullable = false, unique = true)
    private String studentIdentification;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "grade_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @Column(name = "medical_information")
    private String medicalInformation;

    @ManyToOne
    @JoinColumn(name = "teacher_responsible_id")
    private Teacher teacherResponsible;

    @OneToOne(mappedBy = "studentResponsible")
    private Parent parentResponsible;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Course> courses;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", insertable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
