package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.dto.EventFeedDTO;
import com.cts.mfrp.anvay.entity.Event;
import com.cts.mfrp.anvay.entity.EventParticipant;

public interface EventService {
    Event createEvent(Event event);
    Event getEventById(Long eventId);
    List<Event> getEventsByClubId(Long clubId);
    Event updateEvent(Long eventId, Event event);
    void deleteEvent(Long eventId);
    List<Event> getAllEvents();
    EventParticipant registerParticipant(EventParticipant participant);
    List<EventFeedDTO> getAllEventsWithStatus(Long userId);
}
