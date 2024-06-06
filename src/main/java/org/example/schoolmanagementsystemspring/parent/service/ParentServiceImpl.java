package org.example.schoolmanagementsystemspring.parent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationService;
import org.example.schoolmanagementsystemspring.parent.dto.ParentResponse;
import org.example.schoolmanagementsystemspring.parent.dto.RequestParent;
import org.example.schoolmanagementsystemspring.parent.dto.StudentResponse;
import org.example.schoolmanagementsystemspring.parent.entity.Parent;
import org.example.schoolmanagementsystemspring.parent.repository.ParentRepository;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.student.entity.Student;
import org.example.schoolmanagementsystemspring.student.repository.StudentRepository;
import org.example.schoolmanagementsystemspring.teacher.entity.Teacher;
import org.example.schoolmanagementsystemspring.user.entity.Role;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ParentServiceImpl implements ParentService {

    private final ParentRepository repository;
    private final PasswordEncoder encoder;
    private final StudentRepository studentRepository;
    private final AuthenticationService authenticationService;

    @Override
    public void register(RequestParent request) throws UserAlreadyExistsException, UserNotFoundException, SchoolNotFoundException {
        log.info("Registering parent: {}", request.email());

        boolean hasParent = repository.existsByEmail(request.email());

        if (hasParent)
            throw new UserAlreadyExistsException("Parent already exists");

        Parent parent = Parent
                .builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .role(Role.PARENT)
                .description(request.description())
                .phoneNumber(request.phoneNumber())
                .type(request.type())
                .occupation(request.occupation())
                .isEnabled(false)
                .build();

        parent.setPassword(encoder.encode(parent.getPassword()));

        repository.save(parent);

        authenticationService.generateActivationCode(request.email());
    }

    @Override
    public ParentResponse getParentInformation(Authentication authentication) throws UserNotFoundException {
        log.info("Getting parent {} information", authentication.getName());

        var username = authentication.getName();

        Parent parent = repository
                .findByEmailAndIsEnabledTrue(username)
                .orElseThrow(() -> new UserNotFoundException("Parent not found"));

        var studentResponsible = parent.getStudentResponsible();

        if (studentResponsible == null)
            studentResponsible = new Student();

        return ParentResponse
                .builder()
                .id(parent.getId())
                .firstName(parent.getFirstName())
                .lastName(parent.getLastName())
                .email(parent.getEmail())
                .description(parent.getDescription())
                .phoneNumber(parent.getPhoneNumber())
                .type(parent.getType())
                .occupation(parent.getOccupation())
                .studentResponsible(
                        ParentResponse.StudentDto
                                .builder()
                                .id(parent.getStudentResponsible().getId())
                                .firstName(parent.getStudentResponsible().getFirstName())
                                .lastName(parent.getStudentResponsible().getLastName())
                                .email(parent.getStudentResponsible().getEmail())
                                .description(parent.getStudentResponsible().getDescription())
                                .studentIdentification(parent.getStudentResponsible().getStudentIdentification())
                                .birthDate(parent.getStudentResponsible().getBirthDate())
                                .gradeLevel(parent.getStudentResponsible().getGradeLevel())
                                .medicalInformation(parent.getStudentResponsible().getMedicalInformation())
                                .build()
                )
                .build();
    }

    @Override
    public void associateStudentFor(Authentication authentication, String studentEmail) throws UserNotFoundException {
        log.info("Associating student {} to parent {}", studentEmail, authentication.getName());

        var parent = repository
                .findByEmailAndIsEnabledTrue(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Parent not found"));

        var student = studentRepository
                .findByEmailAndIsEnabledTrue(studentEmail)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if (parent.getStudentResponsible() != null)
            throw new IllegalArgumentException("Parent already has a student associated");

        parent.setStudentResponsible(student);

        repository.save(parent);
    }

    @Override
    public StudentResponse getStudentFor(Authentication authentication) throws UserNotFoundException {
        log.info("Getting student for parent {}", authentication.getName());

        var parent = repository
                .findByEmailAndIsEnabledTrue(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Parent not found"));

        var student = parent.getStudentResponsible();

        if (student == null)
            throw new IllegalArgumentException("Parent does not have a student associated");

        var teacher = student.getTeacherResponsible();

        if (teacher == null)
            teacher = new Teacher();

        var school = student.getSchool();

        if (school == null)
            school = new School();

        return StudentResponse
                .builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .description(student.getDescription())
                .studentIdentification(student.getStudentIdentification())
                .birthDate(student.getBirthDate())
                .gradeLevel(student.getGradeLevel())
                .medicalInformation(student.getMedicalInformation())
                .teacherResponsible(
                        StudentResponse.TeacherDto
                                .builder()
                                .firstName(teacher.getFirstName())
                                .lastName(teacher.getLastName())
                                .email(teacher.getEmail())
                                .description(teacher.getDescription())
                                .phoneNumber(teacher.getPhoneNumber())
                                .department(teacher.getDepartment())
                                .educationQualification(teacher.getEducationQualification())
                                .experience(teacher.getExperience())
                                .recognition(teacher.getRecognition())
                                .teachMethod(teacher.getTeachMethod())
                                .build()
                )
                .school(
                        StudentResponse.SchoolDto
                                .builder()
                                .email(school.getEmail())
                                .phoneNumber(school.getPhoneNumber())
                                .facebookURL(school.getFacebookURL())
                                .instagramURL(school.getInstagramURL())
                                .address(school.getAddress())
                                .city(school.getCity())
                                .zipCode(school.getZipCode())
                                .name(school.getName())
                                .build()
                )
                .courses(
                        student.getCourses()
                                .stream()
                                .map(param -> StudentResponse.CourseDto
                                        .builder()
                                        .courseName(param.getCourseName())
                                        .courseDescription(param.getCourseDescription())
                                        .preRequisites(param.getPreRequisites())
                                        .room(param.getRoom())
                                        .courseCode(param.getCourseCode())
                                        .gradeLevel(param.getGradeLevel())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}
