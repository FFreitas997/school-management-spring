package org.example.schoolmanagementsystemspring.course;

import jakarta.persistence.*;
import lombok.*;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.student.GradeLevel;
import org.example.schoolmanagementsystemspring.student.Student;
import org.example.schoolmanagementsystemspring.teacher.Teacher;
import org.example.schoolmanagementsystemspring.textbook.TextBook;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
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
@Builder
@Entity
@Table(name = "course")
@EntityListeners(AuditingEntityListener.class)
public class Course {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_description")
    private String courseDescription;

    @Column(name = "pre_requisites")
    private String preRequisites;

    @Column(name = "room")
    private String room;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "grade_level", nullable = false)
    private GradeLevel gradeLevel;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToMany(mappedBy = "enrolledCourses")
    private List<Student> students;

    @ManyToMany
    @JoinTable(
            name = "course_textbook",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "textbook_id")
    )
    private List<TextBook> textBooks;

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
