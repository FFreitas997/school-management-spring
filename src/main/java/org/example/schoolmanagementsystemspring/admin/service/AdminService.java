package org.example.schoolmanagementsystemspring.admin.service;

import org.example.schoolmanagementsystemspring.admin.dto.*;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * The AdminService interface provides the contract for the admin service.
 * It declares a method for registering a new admin user.
 * Implementations of this interface are expected to provide the business logic for admin operations.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface AdminService {

    void register(RequestUser request) throws UserAlreadyExistsException, UserNotFoundException;

    Page<ResponseTeacher> getTeachers(int page, int size);

    Page<ResponseStudent> getStudents(int page, int size);

    Page<ResponseParent> getParents(int page, int size);

    Page<ResponseCourse> getCourses(int page, int size);

    void createSchool(RequestSchool request);

    // Anyone
    List<SchoolResponse> getSchools();

    // ADMIN
    void createEvent(RequestEvent request);

    // ADMIN
    Page<EventResponse> getEvents(Integer schoolId, Integer page, Integer size);

    void createCourse(RequestCourse request);

    void associateTeacherToCourse(String courseCode, String teacherEmail);

    AdminResponse getAdminInformation(Authentication authentication) throws UserNotFoundException;
}
