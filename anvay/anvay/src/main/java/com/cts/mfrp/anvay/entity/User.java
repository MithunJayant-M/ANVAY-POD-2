package com.cts.mfrp.anvay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "institution_id")
    private Long institutionId;

    // Inside User.java
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_id", insertable = false, updatable = false)
    private Institution institution;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- ADD THESE FIELDS FOR THE DASHBOARD ---
    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "rank_in_leaderboard")
    private Integer rankInLeaderboard;

    @Column(name = "registered_events_count")
    private Integer registeredEventsCount;

    @Column(name = "joined_clubs_count")
    private Integer joinedClubsCount;
    // ------------------------------------------
}
