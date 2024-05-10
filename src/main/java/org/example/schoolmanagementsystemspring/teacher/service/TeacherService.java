package org.example.schoolmanagementsystemspring.teacher.service;

import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.teacher.dto.*;
import org.example.schoolmanagementsystemspring.teacher.exception.StudentAlreadyHasResponsableException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherAlreadyExistsException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherNotFoundException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface TeacherService {

    // TODO method to create textbook, associate to course, list per course, download and upload cover

    void register(RequestTeacher request) throws UserAlreadyExistsException, UserNotFoundException, SchoolNotFoundException, TeacherAlreadyExistsException;

    TeacherResponse getTeacherInformation(Authentication authentication) throws TeacherNotFoundException;

    void associateStudentResponsibleFor(Authentication authentication, String studentEmail) throws TeacherNotFoundException, UserNotFoundException, StudentAlreadyHasResponsableException;

    List<StudentResponse> getStudentResponsibleFor(Authentication authentication) throws TeacherNotFoundException;

    Page<CourseResponse> getCourses(Authentication authentication, int page, int size);

    Page<StudentResponse> searchStudent(String firstName, String lastName, String email, int page, int size);

    void associateStudentsToCourse(String courseCode, List<String> studentsEmail);

    Page<StudentResponse> getStudentsByCourse(String courseCode, int page, int size);

    CourseResponse getCourseInformation(String courseCode);

    void createAssignment(RequestAssignment request);

    Page<AssignmentResponse> getAssignmentsByCourseAndDelivery(String courseCode, LocalDateTime delivery, int page, int size);

    AssignmentResponse getAssignmentByCourseAndDeliveryAndStudent(String courseCode, LocalDateTime delivery, Integer studentID);


    // give grade to student and feedback
    // disable assigment
}
