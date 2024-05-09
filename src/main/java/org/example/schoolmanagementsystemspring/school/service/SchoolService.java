package org.example.schoolmanagementsystemspring.school.service;

import org.example.schoolmanagementsystemspring.school.dto.RequestSchool;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.springframework.data.domain.Page;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public interface SchoolService {

    SchoolResponse createSchool(RequestSchool requestSchool);

    SchoolResponse getSchool(Integer id);

    Page<SchoolResponse> getSchools(Integer page, Integer size);
}
