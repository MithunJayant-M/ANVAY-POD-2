package com.cts.mfrp.anvay.controller;

import com.cts.mfrp.anvay.dto.EventFeedDTO;
import com.cts.mfrp.anvay.entity.Event;
import com.cts.mfrp.anvay.entity.EventParticipant;
import com.cts.mfrp.anvay.repository.EventParticipantRepository;
import com.cts.mfrp.anvay.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Event>> getEventsByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(eventService.getEventsByClubId(clubId));
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(event));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, event));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventFeedDTO>> getAllEvents(@RequestParam(required = false) Long userId) {
        // If no userId is provided (guest view), you can pass a null or 0
        // But for your testing, ensure userId 101 is being passed
        return ResponseEntity.ok(eventService.getAllEventsWithStatus(userId));
    }

    @Autowired
    private EventParticipantRepository participantRepository;

    @PostMapping("/register")
    public ResponseEntity<EventParticipant> registerParticipant(@RequestBody EventParticipant participant) {
        EventParticipant savedParticipant = eventService.registerParticipant(participant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipant);
    }

}
