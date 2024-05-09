package org.example.schoolmanagementsystemspring.teacher.mappers;

import org.example.schoolmanagementsystemspring.assignment.Assignment;
import org.example.schoolmanagementsystemspring.assignment.AssignmentID;
import org.example.schoolmanagementsystemspring.teacher.dto.RequestAssignment;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Component
public class AssignmentMapper implements BiFunction<RequestAssignment, Integer, Assignment> {

    @Override
    public Assignment apply(RequestAssignment requestAssignment, Integer studentID) {
        AssignmentID id = AssignmentID.builder()
                .studentID(studentID)
                .courseCode(requestAssignment.courseCode())
                .deliverAssignment(requestAssignment.deliverAssignment())
                .build();

        return Assignment.builder()
                .id(id)
                .title(requestAssignment.title())
                .description(requestAssignment.description())
                .points(requestAssignment.points())
                .submissionType(requestAssignment.submissionType())
                .assignmentType(requestAssignment.assignmentType())
                .enabled(true)
                .build();
    }
}
