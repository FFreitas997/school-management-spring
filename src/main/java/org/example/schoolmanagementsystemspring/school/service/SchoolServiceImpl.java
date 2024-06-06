package org.example.schoolmanagementsystemspring.school.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.event.Event;
import org.example.schoolmanagementsystemspring.event.EventRepository;
import org.example.schoolmanagementsystemspring.school.dto.EventResponse;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.example.schoolmanagementsystemspring.school.mapper.EventResponseMapper;
import org.example.schoolmanagementsystemspring.school.mapper.SchoolResponseMapper;
import org.example.schoolmanagementsystemspring.school.repository.SchoolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository repository;
    private final EventRepository eventRepository;
    private final SchoolResponseMapper schoolResponseMapper;
    private final EventResponseMapper eventResponseMapper;

    /**
     * This method retrieves all schools.
     *
     * @return a list of all schools
     */
    @Override
    public List<SchoolResponse> getSchools() {
        log.info("Retrieving all schools");

        return repository.findAll()
                .stream()
                .map(schoolResponseMapper)
                .toList();
    }

    /**
     * This method retrieves all future events associated with a specific school.
     * It uses the school id to find the events.
     *
     * @param schoolId the id of the school
     * @return a list of future events associated with the school
     */
    @Override
    public List<EventResponse> getFutureEvents(Integer schoolId) {
        Predicate<Event> isFuture = event -> LocalDateTime.now().isBefore(event.getStart());

        return eventRepository.findBySchoolID(schoolId)
                .stream()
                .filter(isFuture)
                .map(eventResponseMapper)
                .toList();
    }
}
