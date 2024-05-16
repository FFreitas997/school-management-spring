package org.example.schoolmanagementsystemspring.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.school.dto.EventResponse;
import org.example.schoolmanagementsystemspring.school.dto.RequestEvent;
import org.example.schoolmanagementsystemspring.school.dto.RequestSchool;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.example.schoolmanagementsystemspring.school.service.SchoolService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * This controller handles all the school related operations.
 * It uses the SchoolService to process the requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/school")
@Tag(name = "School Features", description = "These operations are related to the school access features.")
public class SchoolController {

    private final SchoolService schoolService;

    /**
     * This method allows an admin to create a new school.
     * It takes a RequestSchool object as input which contains the details of the school to be created.
     * The school details are then passed to the service layer for processing.
     *
     * @param requestSchool a RequestSchool object containing the details of the school to be created
     */
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:create')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Create a new school", description = "This operation creates a new school.")
    @PostMapping
    @ResponseStatus(CREATED)
    public void createSchool(@RequestBody @Valid RequestSchool requestSchool) {
        log.info("Creating a new school: {}", requestSchool.name());
        schoolService.createSchool(requestSchool);
    }

    /**
     * This method retrieves a school by its id.
     * It uses the id to find the school.
     *
     * @param id the id of the school
     * @return the school associated with the id
     */
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get a school by id", description = "This operation retrieves a school by its id.")
    @GetMapping("/{id}")
    public SchoolResponse getSchool(@PathVariable Integer id) {
        log.info("Getting school by id: {}", id);
        return schoolService.getSchool(id);
    }

    /**
     * This method retrieves all schools.
     *
     * @return a list of all schools
     */
    @Operation(summary = "Get all schools", description = "This operation retrieves all schools.")
    @GetMapping("/all")
    public List<SchoolResponse> getSchools() {
        log.info("Getting all schools");
        return schoolService.getSchools();
    }

    /**
     * This method allows an admin to create a new event.
     * It takes a RequestEvent object as input which contains the details of the event to be created.
     * The event details are then passed to the service layer for processing.
     *
     * @param request a RequestEvent object containing the details of the event to be created
     */
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:create')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Create a new event", description = "This operation creates a new event.")
    @PostMapping("/event")
    @ResponseStatus(CREATED)
    public void createEvent(@RequestBody @Valid RequestEvent request) {
        log.info("Creating a new event: {}", request.title());
        schoolService.createEvent(request);
    }

    /**
     * This method retrieves all events associated with a specific school.
     * It uses the school id to find the events.
     * The results are paginated, and the page number and size can be specified. If not specified, the default page number is 0 and the default size is 10.
     *
     * @param schoolId the id of the school
     * @param page the page number (optional, default is 0)
     * @param size the page size (optional, default is 10)
     * @return a paginated list of events associated with the school
     */
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:read')")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get all events by school id", description = "This operation retrieves all events by school id.")
    @GetMapping("/{schoolId}/events")
    public Page<EventResponse> getEvents(@PathVariable Integer schoolId, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Getting all events by school id: {}", schoolId);
        return schoolService.getEvents(schoolId, page, size);
    }

    /**
     * This method retrieves all future events associated with a specific school.
     * It uses the school id to find the events.
     *
     * @param schoolId the id of the school
     * @return a list of future events associated with the school
     */
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get all future events by school id", description = "This operation retrieves all future events by school id.")
    @GetMapping("/{schoolId}/events/future")
    public List<EventResponse> getFutureEvents(@PathVariable Integer schoolId) {
        log.info("Getting all future events by school id: {}", schoolId);
        return schoolService.getFutureEvents(schoolId);
    }
}