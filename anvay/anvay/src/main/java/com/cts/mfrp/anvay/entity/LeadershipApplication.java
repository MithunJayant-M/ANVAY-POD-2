package com.cts.mfrp.anvay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leadership_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadershipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer applicationId;

    @Column(name = "club_id", nullable = false)
    private Integer clubId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "experience", columnDefinition = "TEXT")
    private String experience;

    @Column(name = "status", length = 50)
    private String status; // pending, approved, rejected

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @PrePersist
    protected void onCreate() {
        if (appliedAt == null) appliedAt = LocalDateTime.now();
        if (status == null) status = "pending";
    }
}
