package org.example.schoolmanagementsystemspring.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.admin.dto.*;
import org.example.schoolmanagementsystemspring.admin.mapper.UserMapper;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.course.Course;
import org.example.schoolmanagementsystemspring.course.CourseRepository;
import org.example.schoolmanagementsystemspring.event.Event;
import org.example.schoolmanagementsystemspring.event.EventRepository;
import org.example.schoolmanagementsystemspring.parent.repository.ParentRepository;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.school.repository.SchoolRepository;
import org.example.schoolmanagementsystemspring.student.entity.Student;
import org.example.schoolmanagementsystemspring.student.repository.StudentRepository;
import org.example.schoolmanagementsystemspring.teacher.entity.Teacher;
import org.example.schoolmanagementsystemspring.teacher.repository.TeacherRepository;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The AdminServiceImpl class is a service class that implements the AdminService interface.
 * It provides the business logic for admin operations.
 * It is annotated with @Service to indicate that it's a service class,
 * annotation @RequiredArgsConstructor for automatic generation of a constructor with required arguments,
 * and @Transactional to enable Spring's declarative transaction management.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PasswordEncoder encoder;
    private final CourseRepository courseRepository;
    private final SchoolRepository schoolRepository;
    private final EventRepository eventRepository;

    @Override
    public void register(RequestUser request) throws UserAlreadyExistsException, UserNotFoundException {
        log.info("Registering user: {}", request.email());

        boolean hasUser = userRepository.existsByEmail(request.email());

        if (hasUser)
            throw new UserAlreadyExistsException("User already exists");

        User userMapped = userMapper.apply(request);

        userMapped.setPassword(encoder.encode(request.password()));

        userRepository.save(userMapped);

        authenticationService.generateActivationCode(request.email());
    }

    @Override
    public Page<ResponseTeacher> getTeachers(int page, int size) {
        log.info("Request a page of teachers with those parameters: page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName", "lastName").ascending());

        var result = teacherRepository.findAll(pageable);

        return result.map(param ->
                ResponseTeacher
                        .builder()
                        .id(param.getId())
                        .firstName(param.getFirstName())
                        .lastName(param.getLastName())
                        .email(param.getEmail())
                        .description(param.getDescription())
                        .department(param.getDepartment())
                        .educationQualification(param.getEducationQualification())
                        .experience(param.getExperience())
                        .recognition(param.getRecognition())
                        .teachMethod(param.getTeachMethod())
                        .enabled(param.isEnabled())
                        .build()
        );
    }

    @Override
    public Page<ResponseStudent> getStudents(int page, int size) {
        log.info("Request a page of students with those parameters: page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName", "lastName").ascending());

        var result = studentRepository.findAll(pageable);

        return result.map(param ->
                ResponseStudent
                        .builder()
                        .id(param.getId())
                        .firstName(param.getFirstName())
                        .lastName(param.getLastName())
                        .email(param.getEmail())
                        .description(param.getDescription())
                        .isEnabled(param.isEnabled())
                        .studentIdentification(param.getStudentIdentification())
                        .birthDate(param.getBirthDate())
                        .gradeLevel(param.getGradeLevel())
                        .medicalInformation(param.getMedicalInformation())
                        .build()
        );
    }

    @Override
    public Page<ResponseParent> getParents(int page, int size) {
        log.info("Request a page of parents with those parameters: page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName", "lastName").ascending());

        var result = parentRepository.findAll(pageable);

        return result.map(param -> {
                    if (param.getStudentResponsible() == null)
                        param.setStudentResponsible(new Student());

                    return ResponseParent
                            .builder()
                            .id(param.getId())
                            .firstName(param.getFirstName())
                            .lastName(param.getLastName())
                            .email(param.getEmail())
                            .description(param.getDescription())
                            .isEnabled(param.isEnabled())
                            .type(param.getType())
                            .occupation(param.getOccupation())
                            .studentResponsible(
                                    ResponseParent.StudentDto
                                            .builder()
                                            .id(param.getStudentResponsible().getId())
                                            .firstName(param.getStudentResponsible().getFirstName())
                                            .lastName(param.getStudentResponsible().getLastName())
                                            .email(param.getStudentResponsible().getEmail())
                                            .build()
                            )
                            .build();
                }
        );
    }

    @Override
    public Page<ResponseCourse> getCourses(int page, int size) {
        log.info("Request a page of courses with those parameters: page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("courseName").ascending());

        var result = courseRepository.findAll(pageable);

        return result.map(param -> {

                    if (param.getTeacher() == null)
                        param.setTeacher(new Teacher());

                    if (param.getSchool() == null)
                        param.setSchool(new School());

                    return ResponseCourse
                            .builder()
                            .id(param.getId())
                            .courseName(param.getCourseName())
                            .courseDescription(param.getCourseDescription())
                            .preRequisites(param.getPreRequisites())
                            .room(param.getRoom())
                            .courseCode(param.getCourseCode())
                            .gradeLevel(param.getGradeLevel())
                            .teacher(
                                    ResponseCourse.TeacherDto
                                            .builder()
                                            .id(param.getTeacher().getId())
                                            .firstName(param.getTeacher().getFirstName())
                                            .lastName(param.getTeacher().getLastName())
                                            .email(param.getTeacher().getEmail())
                                            .department(param.getTeacher().getDepartment())
                                            .build()
                            )
                            .school(
                                    ResponseCourse.SchoolDto
                                            .builder()
                                            .email(param.getSchool().getEmail())
                                            .zipCode(param.getSchool().getZipCode())
                                            .id(param.getSchool().getId())
                                            .name(param.getSchool().getName())
                                            .schoolType(param.getSchool().getSchoolType())
                                            .build()
                            )
                            .build();
                }
        );
    }

    @Override
    public void createSchool(RequestSchool request) {
        log.info("Creating a new school: {}", request.name());

        boolean hasSchool = schoolRepository.existsByEmail(request.email());

        if (hasSchool) throw new IllegalArgumentException("School email already registered");

        School entity = School.builder()
                .name(request.name())
                .schoolType(request.type())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .facebookURL(request.facebookURL())
                .instagramURL(request.instagramURL())
                .address(request.address())
                .city(request.city())
                .zipCode(request.zipCode())
                .build();

        schoolRepository.save(entity);
    }

    @Override
    public List<SchoolResponse> getSchools() {
        log.info("Request all schools");

        return schoolRepository.findAll()
                .stream()
                .map(param -> SchoolResponse.builder()
                        .id(param.getId())
                        .name(param.getName())
                        .schoolType(param.getSchoolType())
                        .email(param.getEmail())
                        .facebookURL(param.getFacebookURL())
                        .instagramURL(param.getInstagramURL())
                        .address(param.getAddress())
                        .zipCode(param.getZipCode())
                        .build()
                )
                .toList();
    }

    @Override
    public void createEvent(RequestEvent request) {
        log.info("Creating a new event: {}", request.title());

        School school = schoolRepository.findById(request.schoolID())
                .orElseThrow(() -> new IllegalArgumentException("School not found"));

        Event event = Event.builder()
                .title(request.title())
                .description(request.description())
                .start(request.start())
                .end(request.end())
                .type(request.type())
                .build();

        event.setSchool(school);

        eventRepository.save(event);
    }

    @Override
    public Page<EventResponse> getEvents(Integer schoolId, Integer page, Integer size) {
        log.info("Request a page of events with those parameters: schoolId={}, page={}, size={}", schoolId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        var result = eventRepository.findBySchool_Id(schoolId, pageable);

        return result.map(param -> EventResponse.builder()
                .id(param.getId())
                .title(param.getTitle())
                .description(param.getDescription())
                .start(param.getStart())
                .end(param.getEnd())
                .type(param.getType())
                .location(param.getLocation())
                .build()
        );


    }

    @Override
    public void createCourse(RequestCourse request) {
        log.info("Creating a new course: {}", request.courseName());

        boolean hasCourse = courseRepository.existsByCourseCode(request.courseCode());

        if (hasCourse) throw new IllegalArgumentException("Course code already registered");

        Course entity = Course.builder()
                .courseName(request.courseName())
                .courseDescription(request.courseDescription())
                .preRequisites(request.preRequisites())
                .room(request.room())
                .courseCode(request.courseCode())
                .gradeLevel(request.gradeLevel())
                .build();

        courseRepository.save(entity);
    }

    @Override
    public void associateTeacherToCourse(String courseCode, String teacherEmail) {
        log.info("Associating teacher to course: courseCode={}, teacherEmail={}", courseCode, teacherEmail);

        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Teacher teacher = teacherRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        course.setTeacher(teacher);

        courseRepository.save(course);
    }

    @Override
    public AdminResponse getAdminInformation(Authentication authentication) throws UserNotFoundException {
        log.info("Getting admin information");

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return AdminResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .description(user.getDescription())
                .build();
    }
}
