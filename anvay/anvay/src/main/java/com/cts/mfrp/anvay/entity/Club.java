package com.cts.mfrp.anvay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clubs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "club_name", length = 255, nullable = false)
    private String clubName;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "member_count")
    private Integer memberCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (memberCount == null) memberCount = 0;
    }
}
