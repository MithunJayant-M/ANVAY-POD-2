package com.cts.mfrp.anvay.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.cts.mfrp.anvay.dto.StudentDashboardDTO;
import com.cts.mfrp.anvay.entity.User;
import com.cts.mfrp.anvay.entity.Achievement;
import com.cts.mfrp.anvay.repository.AchievementRepository;
import com.cts.mfrp.anvay.repository.UserRepository;
import com.cts.mfrp.anvay.repository.EventRepository;
import com.cts.mfrp.anvay.service.StudentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    // These are automatically injected by @RequiredArgsConstructor
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AchievementRepository achievementRepository;

    @Override
    @Transactional(readOnly = true)
    public User getStudentById(Long studentId) {
        return userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllStudents() {
        return userRepository.findAll();
    }

    @Override
    public User updateStudent(Long studentId, User user) {
        User existing = getStudentById(studentId);
        if (user.getFirstName() != null) existing.setFirstName(user.getFirstName());
        if (user.getLastName() != null) existing.setLastName(user.getLastName());
        existing.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existing);
    }

    /**
     * Maps Database Entities to the StudentDashboardDTO for the UI
     */
    @Override
    @Transactional(readOnly = true)
    public StudentDashboardDTO getFullDashboard(Long studentId) {
        // 1. Fetch User and related Institution
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Fetch Achievements for the "Achievements" card count
        List<Achievement> userAchievements = achievementRepository.findByUserId(studentId);

        // 3. Map Upcoming Events (Limit 2 as per wireframe)
        List<StudentDashboardDTO.EventRecord> upcomingEvents = eventRepository.findAll()
                .stream()
                .limit(2)
                .map(event -> StudentDashboardDTO.EventRecord.builder()
                        .title(event.getEventName())
                        .institution(event.getClub() != null ? event.getClub().getClubName() : "MKCE")
                        .dateTime(event.getEventDate().toString())
                        .type("Event")
                        .build())
                .collect(Collectors.toList());

        // 4. Final Assembly
        return StudentDashboardDTO.builder()
                .firstName(user.getFirstName())
                .institutionName(user.getInstitution() != null ? user.getInstitution().getName() : "MKCE")
                .totalPoints(user.getTotalPoints() != null ? user.getTotalPoints() : 0)
                .rank(user.getRankInLeaderboard() != null ? user.getRankInLeaderboard() : 0)
                .registeredEventsCount(user.getRegisteredEventsCount() != null ? user.getRegisteredEventsCount() : 0)
                .joinedClubsCount(user.getJoinedClubsCount() != null ? user.getJoinedClubsCount() : 0)
                .achievementCount(userAchievements.size()) // New field for the 4th card
                .upcomingEvents(upcomingEvents)
                .joinedClubs(List.of()) // Map from ClubRepository when ready
                .build();

    }
    }

