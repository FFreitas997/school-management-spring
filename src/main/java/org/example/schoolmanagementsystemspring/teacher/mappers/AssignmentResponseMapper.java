package org.example.schoolmanagementsystemspring.teacher.mappers;

import org.example.schoolmanagementsystemspring.assignment.Assignment;
import org.example.schoolmanagementsystemspring.teacher.dto.AssignmentResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Component
public class AssignmentResponseMapper implements Function<Assignment, AssignmentResponse> {


    @Override
    public AssignmentResponse apply(Assignment result) {
        return AssignmentResponse
                .builder()
                .studentID(result.getId().getStudentID())
                .courseCode(result.getId().getCourseCode())
                .delivery(result.getId().getDeliverAssignment())
                .title(result.getTitle())
                .description(result.getDescription())
                .submissionType(result.getSubmissionType())
                .assignmentType(result.getAssignmentType())
                .grade(result.getGrade())
                .feedback(result.getFeedback())
                .build();
    }
}
