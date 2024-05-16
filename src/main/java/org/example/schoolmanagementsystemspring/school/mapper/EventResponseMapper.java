package org.example.schoolmanagementsystemspring.school.mapper;

import org.example.schoolmanagementsystemspring.event.Event;
import org.example.schoolmanagementsystemspring.school.dto.EventResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EventResponseMapper implements Function<Event, EventResponse> {

    @Override
    public EventResponse apply(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .start(event.getStart())
                .end(event.getEnd())
                .type(event.getType())
                .location(event.getLocation())
                .school(EventResponse.SchoolDto.builder()
                        .email(event.getSchool().getEmail())
                        .facebookURL(event.getSchool().getFacebookURL())
                        .instagramURL(event.getSchool().getInstagramURL())
                        .address(event.getSchool().getAddress())
                        .city(event.getSchool().getCity())
                        .zipCode(event.getSchool().getZipCode())
                        .id(event.getSchool().getId())
                        .name(event.getSchool().getName())
                        .schoolType(event.getSchool().getSchoolType())
                        .build()
                )
                .build();
    }
}
