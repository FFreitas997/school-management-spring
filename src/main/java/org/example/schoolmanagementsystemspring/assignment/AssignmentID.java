package org.example.schoolmanagementsystemspring.assignment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

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
@Embeddable
public class AssignmentID {

    @Column(name = "student_id")
    private Integer studentID;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "deliver_assignment")
    private LocalDateTime deliverAssignment;
}
