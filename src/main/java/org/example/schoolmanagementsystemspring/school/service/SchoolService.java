package org.example.schoolmanagementsystemspring.school.service;

import org.example.schoolmanagementsystemspring.school.dto.EventResponse;
import org.example.schoolmanagementsystemspring.school.dto.RequestEvent;
import org.example.schoolmanagementsystemspring.school.dto.RequestSchool;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface SchoolService {

    // Management Event
    // Some is for ADmin and other for any user

    // ADMIN
    void createSchool(RequestSchool requestSchool);

    // Authenticated
    SchoolResponse getSchool(Integer id);

    // Anyone
    List<SchoolResponse> getSchools();

    // ADMIN
    void createEvent(RequestEvent request);

    // ADMIN
    Page<EventResponse> getEvents(Integer schoolId, Integer page, Integer size);

    //Authenticated
    List<EventResponse> getFutureEvents(Integer schoolId);
}
