package org.example.schoolmanagementsystemspring.student.service;

import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.student.dto.*;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface StudentService {

    void register(RequestStudent request) throws SchoolNotFoundException, UserNotFoundException;

    StudentInformation getStudentInformation(Authentication authentication) throws UserNotFoundException;

    Page<CourseResponse> enrolledCourses(Authentication authentication, int page, int size);

    Page<AssignmentResponse> getAllAssignments(Authentication authentication, int page, int size) throws UserNotFoundException;

    List<AssignmentResponse> getFutureAssignments(Authentication authentication) throws UserNotFoundException;

    TeacherResponse getTeacherResponsibleFor(Authentication authentication) throws UserNotFoundException;

    ParentResponse getParentInformation(Authentication authentication) throws UserNotFoundException;
}
