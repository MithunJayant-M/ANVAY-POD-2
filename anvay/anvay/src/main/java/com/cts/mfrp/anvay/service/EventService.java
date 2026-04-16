package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.dto.EventFeedDTO;
import com.cts.mfrp.anvay.entity.Event;
import com.cts.mfrp.anvay.entity.EventParticipant;

public interface EventService {
    List<Event> getEvents();
    Event createEvent(Event event);
    Event getEventById(Long eventId);
    List<Event> getEventsByClubId(Long clubId);
    Event updateEvent(Long eventId, Event event);
    void deleteEvent(Long eventId);
    EventParticipant registerParticipant(EventParticipant participant);
    List<EventFeedDTO> getAllEventsWithStatus(Long userId);
}
