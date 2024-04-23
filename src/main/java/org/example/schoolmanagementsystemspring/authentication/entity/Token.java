package org.example.schoolmanagementsystemspring.authentication.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "tokens")
@EntityListeners(AuditingEntityListener.class)
public class Token {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "token", nullable = false, length = 1024)
    private String value;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.BEARER;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
}
