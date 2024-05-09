package org.example.schoolmanagementsystemspring.teacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.example.schoolmanagementsystemspring.teacher.exception.StudentAlreadyHasResponsableException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherAlreadyExistsException;
import org.example.schoolmanagementsystemspring.teacher.exception.TeacherNotFoundException;
import org.example.schoolmanagementsystemspring.teacher.mappers.*;
import org.example.schoolmanagementsystemspring.teacher.repository.TeacherRepository;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
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

    @Override
    public TeacherResponse getTeacherInformation(Authentication authentication) throws TeacherNotFoundException {
        log.info("Getting teacher {} information", authentication.getName());

        var username = authentication.getName();

        Teacher teacher = repository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        return responseMapper.apply(teacher);
    }

    @Override
    public void associateStudentResponsibleFor(Authentication authentication, String studentEmail) throws TeacherNotFoundException, UserNotFoundException, StudentAlreadyHasResponsableException {
        log.info("Associating student responsible for teacher: {}", authentication.getName());

        var username = authentication.getName();

        Teacher teacher = repository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        Student student = studentRepository
                .findByEmailAndIsEnabledTrue(studentEmail)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + studentEmail));

        if (student.getTeacherResponsible() != null) {
            throw new StudentAlreadyHasResponsableException("Student already has a teacher as responsible");
        }

        student.setTeacherResponsible(teacher);

        studentRepository.save(student);
    }

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

    @Override
    public CourseResponse getCourseInformation(String courseCode) {
        log.info("Getting course information: {}", courseCode);

        var course = courseRepository
                .findByCourseCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return courseResponseMapper.apply(course);
    }

    @Override
    public void createAssignment(RequestAssignment request) {
        log.info("Creating assignment: {}", request.title());

        var results = studentRepository.findStudentsByCourseCode(request.courseCode());

        var assignments = results
                .stream()
                .map(student -> assignmentMapper.apply(request, student.getId()))
                .toList();

        assignmentRepository.saveAll(assignments);
    }
}
