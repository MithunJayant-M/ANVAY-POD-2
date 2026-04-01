package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.Event;

public interface EventService {
    List<Event> getEvents();
    Event createEvent(Event event);
    Event getEventById(Long eventId);
    List<Event> getEventsByClubId(Long clubId);
    Event updateEvent(Long eventId, Event event);
    void deleteEvent(Long eventId);
}
