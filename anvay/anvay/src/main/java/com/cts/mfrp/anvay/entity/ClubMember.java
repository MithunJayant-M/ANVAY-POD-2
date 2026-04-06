package com.cts.mfrp.anvay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "club_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "club_id", nullable = false)
    private Integer clubId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "role_in_club", length = 100)
    private String roleInClub; // member, leader, secretary

    @Column(name = "status", length = 50)
    private String status; // active, inactive, pending

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) joinedAt = LocalDateTime.now();
        if (status == null) status = "active";
        if (roleInClub == null) roleInClub = "member";
    }
}
