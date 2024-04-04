package org.example.schoolmanagementsystemspring.token;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.schoolmanagementsystemspring.user.User;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "token", nullable = false)
    private String tokenValue;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.BEARER;

    @Column(name = "expired", nullable = false)
    private boolean expired = false;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
}
