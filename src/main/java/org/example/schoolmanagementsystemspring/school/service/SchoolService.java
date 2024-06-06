package org.example.schoolmanagementsystemspring.school.service;

import org.example.schoolmanagementsystemspring.school.dto.EventResponse;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;

import java.util.List;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface SchoolService {

    // Anyone
    List<SchoolResponse> getSchools();

    //Authenticated
    List<EventResponse> getFutureEvents(Integer schoolId);
}
