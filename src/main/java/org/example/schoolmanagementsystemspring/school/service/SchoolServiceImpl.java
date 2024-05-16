package org.example.schoolmanagementsystemspring.school.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.event.Event;
import org.example.schoolmanagementsystemspring.event.EventRepository;
import org.example.schoolmanagementsystemspring.school.dto.EventResponse;
import org.example.schoolmanagementsystemspring.school.dto.RequestEvent;
import org.example.schoolmanagementsystemspring.school.dto.RequestSchool;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.example.schoolmanagementsystemspring.school.mapper.EventMapper;
import org.example.schoolmanagementsystemspring.school.mapper.EventResponseMapper;
import org.example.schoolmanagementsystemspring.school.mapper.SchoolMapper;
import org.example.schoolmanagementsystemspring.school.mapper.SchoolResponseMapper;
import org.example.schoolmanagementsystemspring.school.repository.SchoolRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final EventMapper eventMapper;
    private final SchoolMapper schoolMapper;
    private final SchoolResponseMapper schoolResponseMapper;
    private final EventResponseMapper eventResponseMapper;

    /**
     * This method allows an admin to create a new school.
     * It takes a RequestSchool object as input which contains the details of the school to be created.
     * The school details are then passed to the repository for saving.
     *
     * @param requestSchool a RequestSchool object containing the details of the school to be created
     */
    @Override
    public void createSchool(RequestSchool requestSchool) {
        log.info("Creating a new school: {}", requestSchool.name());

        boolean hasSchool = repository.existsByEmail(requestSchool.email());

        if (hasSchool) throw new IllegalArgumentException("School email already registered");

        School entity = schoolMapper.apply(requestSchool);

        repository.save(entity);
    }

    /**
     * This method retrieves a school by its id.
     * It uses the id to find the school.
     *
     * @param id the id of the school
     * @return the school associated with the id
     */
    @Override
    public SchoolResponse getSchool(Integer id) {
        return repository.findById(id)
                .map(schoolResponseMapper)
                .orElseThrow(() -> new IllegalArgumentException("School not found"));
    }

    /**
     * This method retrieves all schools.
     *
     * @return a list of all schools
     */
    @Override
    public List<SchoolResponse> getSchools() {
        return repository.findAll()
                .stream()
                .map(schoolResponseMapper)
                .toList();
    }

    /**
     * This method allows an admin to create a new event.
     * It takes a RequestEvent object as input which contains the details of the event to be created.
     * The event details are then passed to the repository for saving.
     *
     * @param request a RequestEvent object containing the details of the event to be created
     */
    @Override
    public void createEvent(RequestEvent request) {
        log.info("Creating a new event: {}", request.title());

        School school = repository.findById(request.schoolID())
                .orElseThrow(() -> new IllegalArgumentException("School not found"));

        Event event = eventMapper.apply(request);

        event.setSchool(school);

        eventRepository.save(event);
    }

    /**
     * This method retrieves all events associated with a specific school.
     * It uses the school id to find the events.
     * The results are paginated, and the page number and size can be specified. If not specified, the default page number is 0 and the default size is 10.
     *
     * @param schoolId the id of the school
     * @param page     the page number (optional, default is 0)
     * @param size     the page size (optional, default is 10)
     * @return a paginated list of events associated with the school
     */
    @Override
    public Page<EventResponse> getEvents(Integer schoolId, Integer page, Integer size) {
        log.info("Getting events from school: {}", schoolId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("start").ascending());

        return eventRepository.findBySchool_Id(schoolId, pageable)
                .map(eventResponseMapper);
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
