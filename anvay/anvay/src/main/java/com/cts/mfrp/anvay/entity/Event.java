package com.cts.mfrp.anvay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "participant_type", length = 100)
    private String participantType; // open, members_only, institution_only

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "registration_fee")
    private Double registrationFee;

    @Column(name = "status", length = 50)
    private String status; // upcoming, ongoing, completed, cancelled

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = "upcoming";
    }
}
