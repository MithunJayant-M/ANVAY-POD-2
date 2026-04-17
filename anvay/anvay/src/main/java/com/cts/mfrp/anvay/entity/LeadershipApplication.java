package com.cts.mfrp.anvay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leadership_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadershipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "experience", columnDefinition = "TEXT")
    private String experience;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @PrePersist
    protected void onCreate() {
        if (appliedAt == null) appliedAt = LocalDateTime.now();
        if (status == null) status = "pending";
    }
}
