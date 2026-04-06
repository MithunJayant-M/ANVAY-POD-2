package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.Event;

public interface EventService {
    Event createEvent(Event event);
    Event getEventById(Integer eventId);
    List<Event> getEventsByInstitutionId(Integer institutionId);
    Event updateEvent(Integer eventId, Event event);
    void deleteEvent(Integer eventId);
}
