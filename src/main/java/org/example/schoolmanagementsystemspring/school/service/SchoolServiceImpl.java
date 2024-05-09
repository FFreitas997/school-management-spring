package org.example.schoolmanagementsystemspring.school.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.school.dto.RequestSchool;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.example.schoolmanagementsystemspring.school.repository.SchoolRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository repository;

    @Override
    public SchoolResponse createSchool(RequestSchool requestSchool) {
        return null;
    }

    @Override
    public SchoolResponse getSchool(Integer id) {
        return null;
    }

    @Override
    public Page<SchoolResponse> getSchools(Integer page, Integer size) {
        return null;
    }
}
