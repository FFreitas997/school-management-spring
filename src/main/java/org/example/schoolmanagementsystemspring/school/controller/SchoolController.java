package org.example.schoolmanagementsystemspring.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.school.dto.EventResponse;
import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.example.schoolmanagementsystemspring.school.service.SchoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * This method retrieves all schools.
     *
     * @return a list of all schools
     */
    @Operation(summary = "Get all schools", description = "This operation retrieves all schools and it doesn't require any authentication.")
    @GetMapping("/all")
    public List<SchoolResponse> getSchools() {
        log.info("Getting all schools");
        return schoolService.getSchools();
    }

    /**
     * This method retrieves all future events associated with a specific school.
     * It uses the school id to find the events.
     *
     * @param schoolId the id of the school
     * @return a list of future events associated with the school
     */
    @SecurityRequirement(name = "JSON Web Token (JWT)")
    @Operation(summary = "Get all future events by school id", description = "This operation retrieves all future events by school id.")
    @GetMapping("/{schoolId}/events/future")
    public List<EventResponse> getFutureEvents(@PathVariable Integer schoolId) {
        log.info("Getting all future events by school id: {}", schoolId);
        return schoolService.getFutureEvents(schoolId);
    }
}