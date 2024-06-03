package org.example.schoolmanagementsystemspring.parent.service;

import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.parent.dto.ParentResponse;
import org.example.schoolmanagementsystemspring.parent.dto.RequestParent;
import org.example.schoolmanagementsystemspring.parent.dto.StudentResponse;
import org.example.schoolmanagementsystemspring.school.exception.SchoolNotFoundException;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;

public interface ParentService {

    void register(RequestParent request) throws UserAlreadyExistsException, UserNotFoundException, SchoolNotFoundException;

    ParentResponse getParentInformation(Authentication authentication) throws UserNotFoundException;

    void associateStudentFor(Authentication authentication, String studentEmail) throws UserNotFoundException;

    StudentResponse getStudentFor(Authentication authentication) throws UserNotFoundException;

}
