package org.example.schoolmanagementsystemspring.assignment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Embeddable
public class AssignmentID {

    @Column(name = "student_id")
    private Integer studentID;

    @Column(name = "course_id")
    private Integer courseID;

    @Column(name = "deliver_assignment")
    private LocalDateTime deliverAssignment;
}
