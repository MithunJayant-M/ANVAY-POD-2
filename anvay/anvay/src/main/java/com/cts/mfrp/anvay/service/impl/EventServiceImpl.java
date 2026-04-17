package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.dto.EventFeedDTO;
import com.cts.mfrp.anvay.entity.Event;
import com.cts.mfrp.anvay.entity.EventParticipant;
import com.cts.mfrp.anvay.entity.User;
import com.cts.mfrp.anvay.repository.EventParticipantRepository;
import com.cts.mfrp.anvay.repository.EventRepository;
import com.cts.mfrp.anvay.repository.UserRepository;
import com.cts.mfrp.anvay.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public List<Event> getEventsByInstitutionId(Long institutionId) {
        return eventRepository.findByInstitutionId(institutionId);
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
        if (event.getMaxParticipants() != null) existing.setMaxParticipants(event.getMaxParticipants());
        if (event.getRegistrationFee() != null) existing.setRegistrationFee(event.getRegistrationFee());
        existing.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(existing);
    }

    @Override
    public void deleteEvent(Long eventId) {

        eventRepository.deleteById(eventId);
    }

    // Inside EventServiceImpl.java
    private final EventParticipantRepository participantRepository; // Add this to constructor
    private final UserRepository userRepository;

    @Override
    public EventParticipant registerParticipant(EventParticipant participant) {
        log.info("Registering user {} for event {}", participant.getUserId(), participant.getEventId());

        // 1. Save the registration record
        participant.setCreatedAt(LocalDateTime.now());
        participant.setStatus("REGISTERED");
        EventParticipant savedParticipant = participantRepository.save(participant);

        // 2. Update User table (registered_events_count)
        User user = userRepository.findById(participant.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        int currentEventCount = user.getRegisteredEventsCount() != null ? user.getRegisteredEventsCount() : 0;
        user.setRegisteredEventsCount(currentEventCount + 1);
        userRepository.save(user);

        // 3. Update Event table (registered_count)
        Event event = eventRepository.findById(participant.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

//        int currentRegCount = event.getEr() != null ? event.getEr().size() : 0;
//        event.setRegisteredCount(currentRegCount + 1);
//        eventRepository.save(event);

        return savedParticipant;
    }

    @Override
    public List<EventFeedDTO> getAllEventsWithStatus(Long userId) {
        List<Object[]> results = eventRepository.findAllEventsWithRegistrationStatus(userId);
        return mapToFeedDTOs(results);
    }

    @Override
    public List<EventFeedDTO> getEventsForStudent(Long userId, Long institutionId) {
        List<Object[]> results = eventRepository.findEventsForStudent(userId, institutionId);
        return mapToFeedDTOs(results);
    }

    private List<EventFeedDTO> mapToFeedDTOs(List<Object[]> results) {
        return results.stream().map(result -> {
            Event event = (Event) result[0];
            Boolean isRegistered = (Boolean) result[1];
            String institutionName = event.getClub() != null && event.getClub().getInstitution() != null
                    ? event.getClub().getInstitution().getName() : "General";
            Long institutionId = event.getClub() != null ? event.getClub().getInstitutionId() : null;
            return EventFeedDTO.builder()
                    .eventId(event.getEventId())
                    .title(event.getEventName())
                    .institution(institutionName)
                    .institutionId(institutionId)
                    .location(event.getLocation())
                    .type(event.getCategory())
                    .participantType(event.getParticipantType())
                    .registeredCount(event.getEr() != null ? event.getEr().size() : 0)
                    .totalCapacity(event.getMaxParticipants())
                    .isRegistered(isRegistered)
                    .build();
        }).collect(Collectors.toList());
    }
}
