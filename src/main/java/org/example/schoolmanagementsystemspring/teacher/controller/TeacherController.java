package org.example.schoolmanagementsystemspring.teacher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.teacher.dto.*;
import org.example.schoolmanagementsystemspring.teacher.exception.StudentAlreadyHasResponsibleException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherAlreadyExistsException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherNotFoundException;
import org.example.schoolmanagementsystemspring.teacher.service.TeacherService;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

/**
 * This class is a Controller for managing teacher-related operations.
 * It provides endpoints for registering a teacher, getting teacher information, searching for a student,
 * associating a student to a teacher, getting all students associated with a teacher, getting courses associated with a teacher,
 * associating students to a course, getting students associated with a course, getting information about a course,
 * creating an assignment, getting assignments by course and delivery date, getting an assignment by course, delivery date and student,
 * submitting feedback for an assignment, and disabling an assignment.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teacher")
@Tag(name = "Teacher Features", description = "These operations allow the Teacher user to manage their account.")
public class TeacherController {

    private final TeacherService service;

    /**
     * This method allows a teacher to register in the system.
     *
     * @param request the request containing the teacher's information
     * @throws UserNotFoundException         if the user is not found
     * @throws UserAlreadyExistsException    if the user already exists
     * @throws SchoolNotFoundException       if the school is not found
     * @throws TeacherAlreadyExistsException if the teacher already exists
     */
    @Operation(summary = "Teacher Registration", description = "Allow the Teacher user to register in the system.")
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void registerTeacher(@RequestBody @Valid RequestTeacher request) throws UserNotFoundException, UserAlreadyExistsException, SchoolNotFoundException, TeacherAlreadyExistsException {
        log.info("Registering teacher: {}", request.email());
        service.register(request);
    }


    /**
     * This method retrieves the information of the authenticated teacher.
     * It uses the authentication object to get the teacher's username and then retrieves the teacher's information.
     * If the teacher is not found, it throws a TeacherNotFoundException.
     *
     * @param authentication the authentication object containing the teacher's username
     * @return the teacher's information
     * @throws TeacherNotFoundException if the teacher is not found
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Teacher Information", description = "Get Teacher Information")
    @GetMapping
    @ResponseStatus(OK)
    public TeacherResponse getTeacherInformation(Authentication authentication) throws TeacherNotFoundException {
        log.info("Getting teacher: {}", authentication.getName());
        return service.getTeacherInformation(authentication);
    }

    /**
     * This method allows a teacher to search for a student by first name, last name, or email.
     * It returns a paginated list of students that match the search criteria.
     * If no search criteria are provided, it returns all students.
     *
     * @param firstName the first name of the student (optional)
     * @param lastName  the last name of the student (optional)
     * @param email     the email of the student (optional)
     * @param page      the page number (optional, default is 0)
     * @param size      the page size (optional, default is 10)
     * @return a paginated list of students that match the search criteria
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Search Student", description = "Search for a student by first name, last name or email")
    @GetMapping("/student/search")
    @ResponseStatus(OK)
    public Page<StudentResponse> searchStudent(@RequestParam(required = false, name = "firstName") String firstName, @RequestParam(required = false, name = "lastName") String lastName, @RequestParam(required = false, name = "email") String email, @RequestParam(required = false, name = "page", defaultValue = "0") int page, @RequestParam(required = false, name = "size", defaultValue = "10") int size) {
        log.info("Searching for student: {} {} {} (page: {} || size: {})", firstName, lastName, email, page, size);
        return service.searchStudent(firstName, lastName, email, page, size);
    }

    /**
     * This method allows a teacher to associate a student to themselves.
     * It uses the authentication object to get the teacher's username and the student's email to find the respective entities.
     * If the teacher or student is not found, it throws a TeacherNotFoundException or UserNotFoundException respectively.
     * If the student already has a responsible teacher, it throws a StudentAlreadyHasResponsibleException.
     *
     * @param studentEmail   the email of the student to be associated
     * @param authentication the authentication object containing the teacher's username
     * @throws TeacherNotFoundException              if the teacher is not found
     * @throws UserNotFoundException                 if the student is not found
     * @throws StudentAlreadyHasResponsibleException if the student already has a responsible teacher
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:update')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Associate Student", description = "Associate a student to the teacher")
    @PatchMapping("/student/responsibility")
    @ResponseStatus(ACCEPTED)
    public void associateStudent(@RequestParam(name = "email") String studentEmail, Authentication authentication) throws TeacherNotFoundException, UserNotFoundException, StudentAlreadyHasResponsibleException {
        log.info("Associating student: {} to teacher: {}", studentEmail, authentication.getName());
        service.associateStudentResponsibleFor(authentication, studentEmail);
    }

    /**
     * This method retrieves all students associated with the authenticated teacher.
     * It uses the authentication object to get the teacher's username and then retrieves all students associated with the teacher.
     * If the teacher is not found, it throws a TeacherNotFoundException.
     *
     * @param authentication the authentication object containing the teacher's username
     * @return a list of students associated with the teacher
     * @throws TeacherNotFoundException if the teacher is not found
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "All associated students", description = "Get all students associated with the teacher")
    @GetMapping("/student/responsibility")
    @ResponseStatus(OK)
    public List<StudentResponse> getAllAssociatedStudents(Authentication authentication) throws TeacherNotFoundException {
        log.info("Getting all students associated with teacher: {}", authentication.getName());
        return service.getStudentResponsibleFor(authentication);
    }

    /**
     * This method retrieves all courses associated with the authenticated teacher.
     * It uses the authentication object to get the teacher's username and then retrieves all courses associated with the teacher.
     * The results are paginated, and the page number and size can be specified. If not specified, the default page number is 0 and the default size is 10.
     *
     * @param auth the authentication object containing the teacher's username
     * @param page the page number (optional, default is 0)
     * @param size the page size (optional, default is 10)
     * @return a paginated list of courses associated with the teacher
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Teacher Courses", description = "Get all courses associated with the teacher")
    @GetMapping("/course")
    @ResponseStatus(OK)
    public Page<CourseResponse> getCourses(Authentication auth, @RequestParam(required = false, name = "page", defaultValue = "0") int page, @RequestParam(required = false, name = "size", defaultValue = "10") int size) {
        log.info("Getting courses associated with teacher: {} (page: {} || size: {})", auth.getName(), page, size);
        return service.getCourses(auth, page, size);
    }

    /**
     * This method allows a teacher to associate students to a course.
     * It uses the course code to find the course and the list of student emails to find the respective students.
     * The students are then associated with the course.
     *
     * @param courseCode    the code of the course to which the students will be associated
     * @param studentsEmail a list of emails of the students to be associated with the course
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:update')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Course and Students Association", description = "Attach students to a course")
    @PatchMapping("/course/student")
    @ResponseStatus(OK)
    public void associateStudentsToCourse(@RequestParam(name = "courseCode") String courseCode, @RequestBody List<String> studentsEmail) {
        log.info("Associating students to course: {}", courseCode);
        service.associateStudentsToCourse(courseCode, studentsEmail);
    }

    /**
     * This method retrieves all students associated with a specific course.
     * It uses the course code to find the course and then retrieves all students associated with the course.
     * The results are paginated, and the page number and size can be specified. If not specified, the default page number is 0 and the default size is 10.
     *
     * @param courseCode the code of the course
     * @param page       the page number (optional, default is 0)
     * @param size       the page size (optional, default is 10)
     * @return a paginated list of students associated with the course
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Students by Course", description = "Get all students associated with a course")
    @GetMapping("/course/{courseCode}/student")
    @ResponseStatus(OK)
    public Page<StudentResponse> getStudentsByCourse(@PathVariable String courseCode, @RequestParam(required = false, name = "page", defaultValue = "0") int page, @RequestParam(required = false, name = "size", defaultValue = "10") int size) {
        log.info("Getting students associated with course: {} (page: {} || size: {})", courseCode, page, size);
        return service.getStudentsByCourse(courseCode, page, size);
    }

    /**
     * This method retrieves information about a specific course.
     * It uses the course code to find the course and then retrieves the course's information.
     *
     * @param courseCode the code of the course
     * @return the course's information
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Course Information", description = "Get information about a course")
    @GetMapping("/course/{courseCode}")
    @ResponseStatus(OK)
    public CourseResponse getCourseInformation(@PathVariable String courseCode) {
        log.info("Getting information about course: {}", courseCode);
        return service.getCourseInformation(courseCode);
    }

    /**
     * This method allows a teacher to create an assignment.
     * It takes a RequestAssignment object as input which contains the details of the assignment to be created.
     * The assignment details are then passed to the service layer for processing.
     *
     * @param request a RequestAssignment object containing the details of the assignment to be created
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:create')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Assignment Creation", description = "Create an assignment")
    @PostMapping("/assignment")
    @ResponseStatus(ACCEPTED)
    public void createAssignment(@RequestBody @Valid RequestAssignment request) {
        log.info("Creating assignment: {}", request.title());
        service.createAssignment(request);
    }

    /**
     * This method retrieves all assignments associated with a specific course and delivery date.
     * It uses the course code and the delivery date to find the assignments.
     * The results are paginated, and the page number and size can be specified. If not specified, the default page number is 0 and the default size is 10.
     *
     * @param courseCode the code of the course
     * @param delivery   the delivery date of the assignments
     * @param page       the page number (optional, default is 0)
     * @param size       the page size (optional, default is 10)
     * @return a paginated list of assignments associated with the course and delivery date
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Assignments by Course and Delivery", description = "Get all assignments by course and delivery date")
    @GetMapping("/assignment")
    @ResponseStatus(OK)
    public Page<AssignmentResponse> getAssignmentsByCourseAndDelivery(@RequestParam(name = "courseCode") String courseCode, @RequestParam(name = "delivery") String delivery, @RequestParam(required = false, name = "page", defaultValue = "0") int page, @RequestParam(required = false, name = "size", defaultValue = "10") int size) {
        log.info("Getting assignments by course: {} and delivery: {} (page: {} || size: {})", courseCode, delivery, page, size);
        return service.getAssignmentsByCourseAndDelivery(courseCode, LocalDateTime.parse(delivery), page, size);
    }

    /**
     * This method retrieves an assignment associated with a specific course, delivery date, and student.
     * It uses the course code, the delivery date, and the student ID to find the assignment.
     *
     * @param courseCode the code of the course
     * @param delivery   the delivery date of the assignment
     * @param studentID  the ID of the student
     * @return the assignment associated with the course, delivery date, and student
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Assignment by Course, Delivery and Student", description = "Get an assignment by course, delivery date and student")
    @GetMapping("/assignment/{courseCode}/{studentID}")
    @ResponseStatus(OK)
    public AssignmentResponse getAssignmentByCourseAndDeliveryAndStudent(@PathVariable String courseCode, @RequestParam(name = "delivery") String delivery, @PathVariable Integer studentID) {
        log.info("Getting assignment by course: {}, delivery: {} and student: {}", courseCode, delivery, studentID);
        return service.getAssignmentByCourseAndDeliveryAndStudent(courseCode, LocalDateTime.parse(delivery), studentID);
    }

    /**
     * This method allows a teacher to submit feedback for an assignment.
     * It takes a RequestFeedback object as input which contains the details of the feedback to be submitted.
     * The feedback details are then passed to the service layer for processing.
     *
     * @param request a RequestFeedback object containing the details of the feedback to be submitted
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:update')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Assignment Feedback", description = "Submit feedback for an assignment")
    @PatchMapping("/assignment/feedback")
    @ResponseStatus(OK)
    public void submitFeedback(@RequestBody @Valid RequestFeedback request) {
        log.info("Submitting feedback for assignment");
        service.submitFeedback(request);
    }

    /**
     * This method allows a teacher to disable an assignment.
     * It uses the course code and the delivery date to find the assignment.
     * Once the assignment is found, it is disabled in the system.
     *
     * @param courseCode the code of the course
     * @param delivery   the delivery date of the assignment
     */
    @PreAuthorize("hasRole('TEACHER') and hasAuthority('teacher:delete')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Assignment Disabling", description = "Disable an assignment")
    @DeleteMapping("/assignment")
    @ResponseStatus(OK)
    public void disableAssignment(@RequestParam(name = "courseCode") String courseCode, @RequestParam(name = "delivery") String delivery) {
        log.info("Disabling assignment: {} - {}", courseCode, delivery);
        service.disableAssignment(courseCode, LocalDateTime.parse(delivery));
    }
}
