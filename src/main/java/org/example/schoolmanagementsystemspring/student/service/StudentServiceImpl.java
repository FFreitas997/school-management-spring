package org.example.schoolmanagementsystemspring.student.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.assignment.AssignmentRepository;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.course.CourseRepository;
import org.example.schoolmanagementsystemspring.parent.ParentRepository;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.school.repository.SchoolRepository;
import org.example.schoolmanagementsystemspring.student.dto.*;
import org.example.schoolmanagementsystemspring.student.entity.Student;
import org.example.schoolmanagementsystemspring.student.mappers.StudentMapper;
import org.example.schoolmanagementsystemspring.student.repository.StudentRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final CourseRepository courseRepository;
    private final AssignmentRepository assignmentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final AuthenticationService authenticationService;
    private final StudentMapper studentMapper;

    @Override
    public void register(RequestStudent request) throws SchoolNotFoundException, UserNotFoundException {
        log.info("Registering student: {}", request.email());

        boolean studentExists = studentRepository.existsByEmail(request.email());

        if (studentExists)
            throw new IllegalStateException("Email already taken");

        School school = schoolRepository
                .findById(request.schoolID())
                .orElseThrow(() -> new SchoolNotFoundException("School not found"));

        Student student = studentMapper.apply(request);

        student.setSchool(school);

        studentRepository.save(student);

        authenticationService.generateActivationCode(request.email());
    }

    @Override
    public StudentInformation getStudentInformation(Authentication authentication) throws UserNotFoundException {
        log.info("Getting student {} information", authentication.getName());

        var username = authentication.getName();

        var student = studentRepository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        return StudentInformation
                .builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .description(student.getDescription())
                .studentIdentification(student.getStudentIdentification())
                .gradeLevel(student.getGradeLevel())
                .medicalInformation(student.getMedicalInformation())
                .build();
    }

    public Page<CourseResponse> enrolledCourses(Authentication authentication, int page, int size) {
        log.info("Getting student {} enrolled courses", authentication.getName());

        var username = authentication.getName();

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedAt").ascending());

        var result = courseRepository.findByStudents_Email(username, pageable);

        return result.map(course ->
                CourseResponse
                        .builder()
                        .id(course.getId())
                        .courseCode(course.getCourseCode())
                        .courseName(course.getCourseName())
                        .courseDescription(course.getCourseDescription())
                        .preRequisites(course.getPreRequisites())
                        .gradeLevel(course.getGradeLevel())
                        .textBooks(course.getTextBooks()
                                .stream()
                                .map(textBook ->
                                        new CourseResponse.TextBookDto(
                                                textBook.getId(),
                                                textBook.getTitle(),
                                                textBook.getDescription(),
                                                textBook.getAuthor(),
                                                textBook.getLink()
                                        )
                                ).toList()
                        )
                        .build()
        );
    }

    @Override
    public Page<AssignmentResponse> getAllAssignments(Authentication authentication, int page, int size) throws UserNotFoundException {
        log.info("Getting student {} assignments", authentication.getName());

        Pageable pageable = PageRequest.of(page, size, Sort.by("deliverAssignment").ascending());

        var username = authentication.getName();

        Student student = studentRepository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        var result = assignmentRepository.findById_StudentID(student.getId(), pageable);

        return result.map(assignment ->
                AssignmentResponse
                        .builder()
                        .studentID(assignment.getId().getStudentID())
                        .courseCode(assignment.getId().getCourseCode())
                        .delivery(assignment.getId().getDeliverAssignment())
                        .grade(assignment.getGrade())
                        .feedback(assignment.getFeedback())
                        .title(assignment.getTitle())
                        .description(assignment.getDescription())
                        .submissionType(assignment.getSubmissionType())
                        .assignmentType(assignment.getAssignmentType())
                        .build()
        );
    }

    @Override
    public List<AssignmentResponse> getFutureAssignments(Authentication authentication) throws UserNotFoundException {
        log.info("Getting student {} future assignments", authentication.getName());

        var username = authentication.getName();

        Student student = studentRepository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        var result = assignmentRepository.findById_StudentIDAndId_DeliverAssignmentAfter(student.getId(), LocalDateTime.now());

        return result.stream().map(assignment ->
                AssignmentResponse
                        .builder()
                        .studentID(assignment.getId().getStudentID())
                        .courseCode(assignment.getId().getCourseCode())
                        .delivery(assignment.getId().getDeliverAssignment())
                        .grade(assignment.getGrade())
                        .feedback(assignment.getFeedback())
                        .title(assignment.getTitle())
                        .description(assignment.getDescription())
                        .submissionType(assignment.getSubmissionType())
                        .assignmentType(assignment.getAssignmentType())
                        .build()
        ).toList();
    }

    @Override
    public TeacherResponse getTeacherResponsibleFor(Authentication authentication) throws UserNotFoundException {
        log.info("Getting student {} teacher responsible for", authentication.getName());

        var result = teacherRepository
                .findByStudentResponsibleFor_Email(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("The student doesn't have a teacher responsible for"));

        return TeacherResponse
                .builder()
                .firstName(result.getFirstName())
                .lastName(result.getLastName())
                .email(result.getEmail())
                .description(result.getDescription())
                .phoneNumber(result.getPhoneNumber())
                .department(result.getDepartment())
                .educationQualification(result.getEducationQualification())
                .experience(result.getExperience())
                .recognition(result.getRecognition())
                .teachMethod(result.getTeachMethod())
                .build();
    }

    @Override
    public ParentResponse getParentInformation(Authentication authentication) throws UserNotFoundException {
        log.info("Getting student {} parent information", authentication.getName());

        var result = parentRepository
                .findByStudentResponsible_Email(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("The student doesn't have a parent responsible for"));

        return ParentResponse
                .builder()
                .firstName(result.getFirstName())
                .lastName(result.getLastName())
                .email(result.getEmail())
                .description(result.getDescription())
                .phoneNumber(result.getPhoneNumber())
                .type(result.getType())
                .occupation(result.getOccupation())
                .build();
    }
}
