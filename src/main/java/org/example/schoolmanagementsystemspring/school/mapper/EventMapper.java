package org.example.schoolmanagementsystemspring.school.mapper;

import org.example.schoolmanagementsystemspring.event.Event;
import org.example.schoolmanagementsystemspring.school.dto.RequestEvent;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EventMapper implements Function<RequestEvent, Event> {

    @Override
    public Event apply(RequestEvent requestEvent) {
        return Event.builder()
                .title(requestEvent.title())
                .description(requestEvent.description())
                .start(requestEvent.start())
                .end(requestEvent.end())
                .type(requestEvent.type())
                .build();
    }
}
