package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.entity.Event;
import com.cts.mfrp.anvay.repository.EventRepository;
import com.cts.mfrp.anvay.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Event createEvent(Event event) {
        log.info("Creating new event: {}", event.getEventName());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEvents(){
        return eventRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByClubId(Long clubId) {
        return eventRepository.findByClubId(clubId);
    }

    @Override
    public Event updateEvent(Long eventId, Event event) {
        Event existing = getEventById(eventId);
        if (event.getEventName() != null) existing.setEventName(event.getEventName());
        if (event.getDescription() != null) existing.setDescription(event.getDescription());
        if (event.getStartDate() != null) existing.setStartDate(event.getStartDate());
        if (event.getEndDate() != null) existing.setEndDate(event.getEndDate());
        if (event.getLocation() != null) existing.setLocation(event.getLocation());
        if (event.getCategory() != null) existing.setCategory(event.getCategory());
        if (event.getStatus() != null) existing.setStatus(event.getStatus());
        if (event.getParticipantType() != null) existing.setParticipantType(event.getParticipantType());
        if (event.getMaxParticipants() != 0) existing.setMaxParticipants(event.getMaxParticipants());
        if (event.getRegistrationFee() != 0) existing.setRegistrationFee(event.getRegistrationFee());
        existing.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(existing);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
