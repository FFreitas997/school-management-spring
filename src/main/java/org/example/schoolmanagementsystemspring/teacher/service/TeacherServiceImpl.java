package org.example.schoolmanagementsystemspring.teacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.assignment.Assignment;
import org.example.schoolmanagementsystemspring.assignment.AssignmentID;
import org.example.schoolmanagementsystemspring.assignment.AssignmentRepository;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.course.Course;
import org.example.schoolmanagementsystemspring.course.CourseRepository;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.school.repository.SchoolRepository;
import org.example.schoolmanagementsystemspring.student.Student;
import org.example.schoolmanagementsystemspring.student.StudentRepository;
import org.example.schoolmanagementsystemspring.teacher.dto.*;
import org.example.schoolmanagementsystemspring.teacher.entity.Teacher;
import org.example.schoolmanagementsystemspring.teacher.exception.StudentAlreadyHasResponsibleException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherAlreadyExistsException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherNotFoundException;
import org.example.schoolmanagementsystemspring.teacher.mappers.*;
import org.example.schoolmanagementsystemspring.teacher.repository.TeacherRepository;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class provides the implementation for the TeacherService interface.
 * It contains methods for managing teachers, students, courses, and assignments.
 * It uses repositories for accessing and manipulating data.
 * It also uses mappers for converting entities to DTOs and vice versa.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;
    private final SchoolRepository schoolRepository;
    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AssignmentRepository assignmentRepository;
    private final TeacherMapper mapper;
    private final TeacherResponseMapper responseMapper;
    private final StudentResponseMapper studentResponseMapper;
    private final CourseResponseMapper courseResponseMapper;
    private final AssignmentMapper assignmentMapper;
    private final AssignmentResponseMapper assignmentResponseMapper;


    /**
     * This method registers a new teacher.
     * It checks if a teacher with the same email already exists.
     * It also checks if the school exists.
     * If the teacher or the school does not exist, it throws an exception.
     * Otherwise, it saves the new teacher and generates an activation code.
     *
     * @param request the request containing the teacher's information
     * @throws UserNotFoundException         if the user is not found
     * @throws SchoolNotFoundException       if the school is not found
     * @throws TeacherAlreadyExistsException if the teacher already exists
     */
    @Override
    public void register(RequestTeacher request) throws UserNotFoundException, SchoolNotFoundException, TeacherAlreadyExistsException {
        log.info("Registering teacher: {}", request.email());

        boolean hasTeacher = repository.existsTeacherByEmail(request.email());

        if (hasTeacher) throw new TeacherAlreadyExistsException("Teacher already exists");

        School school = schoolRepository
                .findById(request.schoolID())
                .orElseThrow(() -> new SchoolNotFoundException("School not found"));

        Teacher newTeacher = mapper.apply(request);

        newTeacher.setSchool(school);

        repository.save(newTeacher);

        authenticationService.generateActivationCode(request.email());
    }

    /**
     * This method retrieves the information of a teacher.
     * It checks if the teacher exists and is enabled.
     * If the teacher does not exist or is not enabled, it throws an exception.
     * Otherwise, it returns the teacher's information.
     *
     * @param authentication the authentication object containing the teacher's username
     * @return the teacher's information
     * @throws TeacherNotFoundException if the teacher is not found
     */
    @Override
    public TeacherResponse getTeacherInformation(Authentication authentication) throws TeacherNotFoundException {
        log.info("Getting teacher {} information", authentication.getName());

        var username = authentication.getName();

        Teacher teacher = repository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        return responseMapper.apply(teacher);
    }

    /**
     * This method associates a student to a teacher.
     * It checks if the teacher and the student exist and are enabled.
     * It also checks if the student already has a teacher responsible for them.
     * If the teacher or the student does not exist, or the student already has a teacher, it throws an exception.
     * Otherwise, it sets the teacher as the student's responsible and saves the student.
     *
     * @param authentication the authentication object containing the teacher's username
     * @param studentEmail   the email of the student to be associated
     * @throws TeacherNotFoundException              if the teacher is not found
     * @throws UserNotFoundException                 if the user is not found
     * @throws StudentAlreadyHasResponsibleException if the student already has a teacher responsible for them
     */
    @Override
    public void associateStudentResponsibleFor(Authentication authentication, String studentEmail) throws TeacherNotFoundException, UserNotFoundException, StudentAlreadyHasResponsibleException {
        log.info("Associating student responsible for teacher: {}", authentication.getName());

        var username = authentication.getName();

        Teacher teacher = repository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        Student student = studentRepository
                .findByEmailAndIsEnabledTrue(studentEmail)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + studentEmail));

        if (student.getTeacherResponsible() != null) {
            throw new StudentAlreadyHasResponsibleException("Student already has a teacher as responsible");
        }

        student.setTeacherResponsible(teacher);

        studentRepository.save(student);
    }

    /**
     * This method retrieves the students a teacher is responsible for.
     * It checks if the teacher exists and is enabled.
     * If the teacher does not exist or is not enabled, it throws an exception.
     * Otherwise, it returns the students the teacher is responsible for.
     *
     * @param authentication the authentication object containing the teacher's username
     * @return the students the teacher is responsible for
     * @throws TeacherNotFoundException if the teacher is not found
     */
    @Override
    public List<StudentResponse> getStudentResponsibleFor(Authentication authentication) throws TeacherNotFoundException {
        log.info("Getting student responsible for teacher: {}", authentication.getName());

        var username = authentication.getName();

        Teacher teacher = repository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        var students = teacher.getStudentResponsibleFor();

        return students.stream()
                .map(studentResponseMapper)
                .toList();
    }

    /**
     * This method retrieves the courses a teacher is teaching.
     * It checks if the teacher exists and is enabled.
     * If the teacher does not exist or is not enabled, it throws an exception.
     * Otherwise, it returns the courses the teacher is teaching.
     *
     * @param authentication the authentication object containing the teacher's username
     * @param page           the page number
     * @param size           the page size
     * @return the courses the teacher is teaching
     */
    @Override
    public Page<CourseResponse> getCourses(Authentication authentication, int page, int size) {
        log.info("Getting courses for teacher: {}", authentication.getName());

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedAt").ascending());

        var username = authentication.getName();

        var result = courseRepository.findByTeacher_EmailAndTeacher_IsEnabledTrue(username, pageable);

        Function<Course, CourseResponse> handleResult = param -> CourseResponse
                .builder()
                .id(param.getId())
                .courseName(param.getCourseName())
                .courseCode(param.getCourseCode())
                .gradeLevel(param.getGradeLevel())
                .build();

        return result.map(handleResult);
    }

    /**
     * This method searches for students by their first name, last name, and email.
     * It returns the students that match the search criteria.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param email     the email of the student
     * @param page      the page number
     * @param size      the page size
     * @return the students that match the search criteria
     */
    @Override
    public Page<StudentResponse> searchStudent(String firstName, String lastName, String email, int page, int size) {
        log.info("Searching student by first name: {}, last name: {}, email: {}", firstName, lastName, email);

        Pageable pageable = PageRequest.of(page, size, Sort.by("email").ascending());

        var result = studentRepository.searchStudent(firstName, lastName, email, pageable);

        Function<Student, StudentResponse> handleResult = param -> StudentResponse
                .builder()
                .firstName(param.getFirstName())
                .lastName(param.getLastName())
                .email(param.getEmail())
                .gradeLevel(param.getGradeLevel())
                .build();

        return result.map(handleResult);
    }

    /**
     * This method associates students to a course.
     * It checks if the course exists.
     * If the course does not exist, it throws an exception.
     * Otherwise, it sets the students to the course and saves the course.
     *
     * @param courseCode    the code of the course
     * @param studentsEmail the emails of the students to be associated
     */
    @Override
    public void associateStudentsToCourse(String courseCode, List<String> studentsEmail) {
        log.info("Associating students to course: {}", courseCode);

        var course = courseRepository
                .findByCourseCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // To void associate students from different grade levels
        Predicate<Student> filterByGradeLevel = param -> {
            var result = param.getGradeLevel() == course.getGradeLevel();
            if (!result)
                log.warn("Student {} is not from the same grade level of the course", param.getEmail());
            return result;
        };

        var students = studentRepository.findByEmailIn(studentsEmail)
                .stream()
                .filter(filterByGradeLevel)
                .toList();

        course.setStudents(students);

        courseRepository.save(course);
    }

    /**
     * This method retrieves the students of a course.
     * It checks if the course exists.
     * If the course does not exist, it throws an exception.
     * Otherwise, it returns the students of the course.
     *
     * @param courseCode the code of the course
     * @param page       the page number
     * @param size       the page size
     * @return the students of the course
     */
    @Override
    public Page<StudentResponse> getStudentsByCourse(String courseCode, int page, int size) {
        log.info("Getting students by course: {}", courseCode);

        Pageable pageable = PageRequest.of(page, size, Sort.by("email").ascending());

        var students = studentRepository.findByCourses_CourseCode(courseCode, pageable);

        Function<Student, StudentResponse> handleResult = param -> StudentResponse
                .builder()
                .firstName(param.getFirstName())
                .lastName(param.getLastName())
                .email(param.getEmail())
                .gradeLevel(param.getGradeLevel())
                .build();

        return students.map(handleResult);
    }

    /**
     * This method retrieves the information of a specific course.
     * It checks if the course exists.
     * If the course does not exist, it throws an IllegalArgumentException.
     * Otherwise, it returns the course's information.
     *
     * @param courseCode the code of the course
     * @return the course's information
     * @throws IllegalArgumentException if the course is not found
     */
    @Override
    public CourseResponse getCourseInformation(String courseCode) {
        log.info("Getting course information: {}", courseCode);

        var course = courseRepository
                .findByCourseCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return courseResponseMapper.apply(course);
    }

    /**
     * This method creates a new assignment.
     * It checks if an assignment with the same course code and delivery date already exists.
     * If the assignment already exists, it logs an error and returns.
     * Otherwise, it finds the students of the course and creates an assignment for each student.
     * It then saves all the assignments.
     *
     * @param request the request containing the assignment's information
     */
    @Override
    public void createAssignment(RequestAssignment request) {
        log.info("Creating assignment: {}", request.title());

        if (assignmentRepository.existsById_CourseCodeAndId_DeliverAssignmentAndEnabledTrue(request.courseCode(), request.deliverAssignment())) {
            log.error("Assignment has already created for course: {} and deliver: {}", request.courseCode(), request.deliverAssignment());
            return;
        }

        var results = studentRepository.findStudentsByCourseCode(request.courseCode());

        var assignments = results
                .stream()
                .map(student -> assignmentMapper.apply(request, student.getId()))
                .toList();

        assignmentRepository.saveAll(assignments);
    }

    /**
     * This method retrieves the assignments of a course by their delivery date.
     * It returns the assignments that match the course code and delivery date.
     *
     * @param courseCode the code of the course
     * @param delivery   the delivery date of the assignments
     * @param page       the page number
     * @param size       the page size
     * @return the assignments that match the course code and delivery date
     */
    @Override
    public Page<AssignmentResponse> getAssignmentsByCourseAndDelivery(String courseCode, LocalDateTime delivery, int page, int size) {
        log.info("Getting assignments by course: {} and delivery: {}", courseCode, delivery);

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedAt").ascending());

        var results = assignmentRepository.findById_CourseCodeAndId_DeliverAssignmentAndEnabledTrue(courseCode, delivery, pageable);

        Function<Assignment, AssignmentResponse> handleResult = param -> AssignmentResponse
                .builder()
                .courseCode(param.getId().getCourseCode())
                .studentID(param.getId().getStudentID())
                .delivery(param.getId().getDeliverAssignment())
                .title(param.getTitle())
                .points(param.getPoints())
                .submissionType(param.getSubmissionType())
                .assignmentType(param.getAssignmentType())
                .build();

        return results.map(handleResult);
    }

    /**
     * This method retrieves the assignment of a student by the course code and delivery date.
     * It checks if the assignment exists.
     * If the assignment does not exist, it throws an IllegalArgumentException.
     * Otherwise, it returns the assignment's information.
     *
     * @param courseCode the code of the course
     * @param delivery   the delivery date of the assignment
     * @param studentID  the ID of the student
     * @return the assignment's information
     * @throws IllegalArgumentException if the assignment is not found
     */
    @Override
    public AssignmentResponse getAssignmentByCourseAndDeliveryAndStudent(String courseCode, LocalDateTime delivery, Integer studentID) {
        log.info("Getting assignment by course: {}, delivery: {} and student: {}", courseCode, delivery, studentID);

        AssignmentID assignmentID = AssignmentID.builder()
                .courseCode(courseCode)
                .studentID(studentID)
                .deliverAssignment(delivery)
                .build();

        var result = assignmentRepository.findById(assignmentID)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        return assignmentResponseMapper.apply(result);
    }

    /**
     * This method submits feedback for an assignment.
     * It checks if the assignment exists.
     * If the assignment does not exist, it throws an IllegalArgumentException.
     * Otherwise, it sets the feedback and grade of the assignment and saves the assignment.
     *
     * @param request the request containing the feedback and grade
     * @throws IllegalArgumentException if the assignment is not found
     */
    @Override
    public void submitFeedback(RequestFeedback request) {
        log.info("Submitting feedback for course: {}, delivery: {} and student: {}", request.courseCode(), request.delivery(), request.studentID());

        AssignmentID assignmentID = AssignmentID.builder()
                .courseCode(request.courseCode())
                .studentID(request.studentID())
                .deliverAssignment(request.delivery())
                .build();

        var assignment = assignmentRepository.findById(assignmentID)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        assignment.setFeedback(request.feedback());
        assignment.setGrade(request.grade());

        assignmentRepository.save(assignment);
    }

    /**
     * This method disables an assignment.
     * It finds the assignments by the course code and delivery date and disables them.
     * It then saves all the assignments.
     *
     * @param courseCode the code of the course
     * @param delivery   the delivery date of the assignments
     */
    @Override
    public void disableAssignment(String courseCode, LocalDateTime delivery) {
        log.info("Disabling assignment for course: {} and delivery: {}", courseCode, delivery);

        var results = assignmentRepository.findById_CourseCodeAndId_DeliverAssignment(courseCode, delivery);

        results.forEach(assignment -> assignment.setEnabled(false));

        assignmentRepository.saveAll(results);

    }
}
