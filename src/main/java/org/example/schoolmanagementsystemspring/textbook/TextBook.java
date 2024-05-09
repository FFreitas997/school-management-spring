package org.example.schoolmanagementsystemspring.textbook;

import jakarta.persistence.*;
import lombok.*;
import org.example.schoolmanagementsystemspring.course.Course;
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
@Table(name = "textbook")
@EntityListeners(AuditingEntityListener.class)
public class TextBook {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 300)
    private String description;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "edition")
    private String edition;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "link")
    private String link;

    @Column(name = "cover_filename")
    private String coverFileName;

    @ManyToMany(mappedBy = "textBooks")
    private List<Course> courses;

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
